package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.model.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AlarmDeviceService {
    Optional<List<AlarmDTO>> getAllAlarms(Long id, int page, int size, String sortBy, String direction);
    AlarmDTO toDTO(Alarm alarm);
    List<AlarmDTO> toListDTO(List<Alarm> alarms);
    Optional<List<AlarmDTO>> getAllManagerAlarms(Authentication authentication, int page, int size, String sortBy, String direction);
    List<AlarmDTO> toPageDTO(Page<Alarm> alarms);
}
