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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlarmDeviceServiceImpl implements AlarmDeviceService {

    private final ManagerRepository managerRepository;
    private final DeviceRepository deviceRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public AlarmDTO toDTO(Alarm alarm) {
        String formattedDateTime = alarm.getAlarmTime().format(AppConstants.CUSTOM_FORMATTER);
        return new AlarmDTO(
                alarm.getDevice().getDeviceId(),
                alarm.getAlarmId(),
                alarm.getAlarmType(),
                alarm.getDescription(),
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
    public Optional<List<AlarmDTO>> getAllAlarms(Long id, int page, int size, String sortBy, String direction) {
        Device device = deviceRepository.findByDeviceId(id);
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        List<Alarm> alarmList = alarmRepository.findByDevice(device, pageable);
        return Optional.of(toListDTO(alarmList));
    }

//    @Override
//    public Optional<List<AlarmDTO>> getAllManagerAlarms(Authentication authentication, Long offset) {
//        Optional<Manager> managerOps = managerRepository.findByEmail(authentication.getName());
//        Manager manager = managerOps.get();
//        List<Device> devices = manager.getDevices();
//        List<List<Alarm>> alarms = new ArrayList<>();
//        for (Device device: devices){
//            alarms.add(device.getAlarms());
//        }
//
//
/// /        offset = offset - 1;
/// /        if (offset >= 1) offset = offset * 20;
//
//        if (offset >= 0) offset = offset * 20;
//        long limit = 20L;
//        List<Alarm> alarmList = alarms.stream()
//                .flatMap(List::stream)
//                .skip(offset)
//                .limit(limit)
//                .sorted(Comparator.comparing(Alarm::getAlarmId).reversed())
//                .toList();
//
//        return Optional.of(toListDTO(alarmList));
//    }

//    @Override
//    public Optional<List<AlarmManagerDTO>> getAllManagerAlarms(Authentication authentication, Long offset) {
//        Optional<Manager> managerOps = managerRepository.findByEmail(authentication.getName());
//        Manager manager = managerOps.get();
//        String direction = "asc";
//        String sortBy = "alarm_id";
//        int page = 0;
//        int size = 20;
//        Sort sort = direction.equalsIgnoreCase("desc") ?
//                Sort.by(sortBy).descending() :
//                Sort.by(sortBy).ascending();
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//        List<AlarmManagerDTO> alarmsList = alarmRepository.findByManagerDevices(manager.getManagerId(), pageable);
//        return Optional.of(alarmsList);
//    }

    @Override
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
