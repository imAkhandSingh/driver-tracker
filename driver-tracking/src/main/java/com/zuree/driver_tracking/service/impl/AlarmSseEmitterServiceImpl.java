package com.zuree.driver_tracking.service.impl;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.dto.request.AlarmRequest;
import com.zuree.driver_tracking.exception.ManagerNotFoundException;
import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.repository.AlarmRepository;
import com.zuree.driver_tracking.repository.DeviceRepository;
import com.zuree.driver_tracking.repository.ManagerRepository;
import com.zuree.driver_tracking.service.AlarmSseEmitterService;
import com.zuree.driver_tracking.util.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlarmSseEmitterServiceImpl implements AlarmSseEmitterService {
    @Value("${preview-url}")
    String previewUrl;

    @Value("${download-url}")
    String downloadUrl;

    String prevUrl;
    String downUrl;

    private final Map<Long, List<SseEmitter>> managerEmitters = new ConcurrentHashMap<>();
    private final ManagerRepository managerRepository;
    private final DeviceRepository deviceRepository;
    private final AlarmRepository alarmRepository;

    public AlarmSseEmitterServiceImpl(ManagerRepository managerRepository, DeviceRepository deviceRepository, AlarmRepository alarmRepository) {
        this.managerRepository = managerRepository;
        this.deviceRepository = deviceRepository;
        this.alarmRepository = alarmRepository;
    }

    @Override
    public SseEmitter streamAlarms(Authentication authentication) {
        String username = authentication.getName();
        Manager manager = managerRepository.findByEmail(username)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found"));

        Long managerId = manager.getManagerId();
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 min

        managerEmitters.computeIfAbsent(managerId, id -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> managerEmitters.get(managerId).remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            managerEmitters.get(managerId).remove(emitter);
        });
        emitter.onError(e -> {
            emitter.complete();
            managerEmitters.get(managerId).remove(emitter);
        });
        return emitter;
    }

    @Override
    public SseEmitter streamAlarmsById(Long mid) {
//        Long managerId = managerRepository.findById(mid).get().getManagerId();
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30 min
        managerEmitters.computeIfAbsent(mid, id -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> managerEmitters.get(mid).remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            managerEmitters.get(mid).remove(emitter);
        });
        emitter.onError(e -> {
            emitter.complete();
            managerEmitters.get(mid).remove(emitter);
        });
        return emitter;
    }

    @Override
    @Transactional
    public Optional<AlarmDTO> createAlarm(AlarmRequest request) {
        Device device = deviceRepository.findByDeviceId(request.deviceId());

        Alarm alarm = Alarm.builder()
                .speed(request.speed())
                .alarmType(request.alarmType())
                .acceleration(request.acceleration())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .drowsiness(request.drowsiness())
                .rashDriving(request.rashDriving())
                .collision(request.collision())
                .description(request.description())
                .alarmTime(LocalDateTime.now(ZoneId.of(AppConstants.ZONE_ID)))
                .device(device)
                .resolved(false)
                .build();

        // ✅ Always save alarm to DB
        alarmRepository.save(alarm);

        // ✅ Create DTO
        String formattedString = LocalDateTime.now().format(AppConstants.CUSTOM_FORMATTER);
        if (alarm.getAlarmImage() == null){
            prevUrl = "Not Available";
            downUrl = "Not Available";
        }else{
            prevUrl = previewUrl+alarm.getAlarmImage().getImageId();
            downUrl = downloadUrl+alarm.getAlarmImage().getImageId();
        }
        AlarmDTO alarmDTO = new AlarmDTO(
                request.deviceId(),
                alarm.getAlarmId(),
                alarm.getSpeed(),
                alarm.getAcceleration(),
                alarm.getLatitude(), alarm.getLongitude(), alarm.getDrowsiness(),
                alarm.getRashDriving(), alarm.getCollision(),
                alarm.getAlarmType(),
                alarm.getDescription(),
                prevUrl,
                downUrl,
                formattedString
        );

        // ✅ Send to active emitters (if any)
        Long managerId = device.getManager().getManagerId();
        List<SseEmitter> emitters = managerEmitters.get(managerId);

        if (emitters != null) {
            // Copy emitters to avoid ConcurrentModificationException
            List<SseEmitter> deadEmitters = new ArrayList<>();
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("alarm").data(alarmDTO));
                } catch (IOException e) {
                    emitter.complete();
                    deadEmitters.add(emitter); // Collect dead ones to remove later
                }
            }
            emitters.removeAll(deadEmitters); // Clean up dead emitters
        }
        return Optional.of(alarmDTO);
    }
}
