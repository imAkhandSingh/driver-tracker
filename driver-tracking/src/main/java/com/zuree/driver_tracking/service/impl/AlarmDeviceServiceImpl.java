package com.zuree.driver_tracking.service.impl;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.repository.AlarmRepository;
import com.zuree.driver_tracking.repository.DeviceRepository;
import com.zuree.driver_tracking.repository.ManagerRepository;
import com.zuree.driver_tracking.service.AlarmDeviceService;
import com.zuree.driver_tracking.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmDeviceServiceImpl implements AlarmDeviceService {

    @Value("${preview-url}")
    String previewUrl;

    @Value("${download-url}")
    String downloadUrl;

    String prevUrl;
    String downUrl;

    private final ManagerRepository managerRepository;
    private final DeviceRepository deviceRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public AlarmDTO toDTO(Alarm alarm) {
        String formattedDateTime = alarm.getAlarmTime().format(AppConstants.CUSTOM_FORMATTER);
        if (alarm.getAlarmImage() == null){
            prevUrl = "Not Available";
            downUrl = "Not Available";
        }else{
            prevUrl = previewUrl+alarm.getAlarmImage().getImageId();
            downUrl = downloadUrl+alarm.getAlarmImage().getImageId();
        }
        return new AlarmDTO(
                alarm.getDevice().getDeviceId(),
                alarm.getAlarmId(),
                alarm.getSpeed(),
                alarm.getAcceleration(),
                alarm.getLatitude(), alarm.getLongitude(), alarm.getDrowsiness(),
                alarm.getRashDriving(), alarm.getCollision(),
                alarm.getAlarmType(),
                alarm.getDescription(),
                prevUrl,
                downUrl,
                formattedDateTime
        );
    }

    @Override
    public List<AlarmDTO> toListDTO(List<Alarm> alarms) {
        return alarms.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AlarmDTO> toPageDTO(Page<Alarm> alarms) {
        return alarms.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public AlarmDTO getAlarmById(Long id) {
        Optional<Alarm> alarm = alarmRepository.findById(id);
        return alarm.map(this::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public Optional<List<AlarmDTO>> getAllAlarms(Long id, int page, int size, String sortBy, String direction) {
        Device device = deviceRepository.findByDeviceId(id);
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        List<Alarm> alarmList = alarmRepository.findByDevice(device, pageable);
        return Optional.of(toListDTO(alarmList));
    }

    @Override
    @Transactional
    public Optional<List<AlarmDTO>> getAllManagerAlarms(Authentication authentication, int page, int size, String sortBy, String direction) {
        Optional<Manager> managerOps = managerRepository.findByEmail(authentication.getName());
        Manager manager = managerOps.get();
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Alarm> alarmsList = alarmRepository.findByManagerId(manager.getManagerId(), pageable);
        return Optional.of(toPageDTO(alarmsList));
    }
}
