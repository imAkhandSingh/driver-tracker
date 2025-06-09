package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.repository.AlarmRepository;
import com.zuree.driver_tracking.repository.DeviceRepository;
import com.zuree.driver_tracking.repository.ManagerRepository;
import com.zuree.driver_tracking.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/alarm/${api.base-version}")
public class ManagerAlarmSseController {

    private final Map<Long, List<SseEmitter>> managerEmitters = new ConcurrentHashMap<>();
    private final ManagerRepository managerRepository;
    private final AlarmRepository alarmRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public ManagerAlarmSseController(ManagerRepository managerRepository, AlarmRepository alarmRepository, DeviceRepository deviceRepository) {
        this.managerRepository = managerRepository;
        this.alarmRepository = alarmRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/stream/{mid}")
    public SseEmitter streamAlarmsForLoggedInManager(@PathVariable Long mid) {
        Optional<Manager> manager = managerRepository.findByManagerId(mid);

        if (manager.isPresent()) {
            Long managerId = manager.get().getManagerId();
            SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
            managerEmitters.computeIfAbsent(managerId, id -> new CopyOnWriteArrayList<>()).add(emitter);

            emitter.onCompletion(() -> managerEmitters.get(managerId).remove(emitter));
            emitter.onTimeout(() -> managerEmitters.get(managerId).remove(emitter));
            return emitter;
        }
        else {
            return null;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> sendAlarmToManager(@RequestBody Map<String, String> body) {
        String alarmType = body.get("alarmType");
        String description = body.get("description");
        Long deviceId = Long.parseLong(body.get("deviceId"));
        Device device = deviceRepository.findByDeviceId(deviceId);

        Alarm alarm = Alarm.builder().alarmType(alarmType).description(description)
                .alarmTime(LocalDateTime.now(ZoneId.of(AppConstants.ZONE_ID)))
                .device(device)
                .resolved(false)
                .build();

        alarmRepository.save(alarm);

        // Copy in map and formatted
        Map<String, String> updatedBody = new HashMap<>(body);
        LocalDateTime ldt = LocalDateTime.now();
        String formattedString = ldt.format(AppConstants.CUSTOM_FORMATTER);
        updatedBody.put("alarmTime", formattedString);

        List<SseEmitter> emitters = managerEmitters.get(device.getManager().getManagerId());
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("alarm").data(updatedBody));
                } catch (IOException e) {
                    emitter.complete();
                }
            }
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("msg", "Alarm successfully added!"));
    }
}

