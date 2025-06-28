package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.dto.request.AlarmRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

public interface AlarmSseEmitterService {
    SseEmitter streamAlarms(Authentication authentication);
    SseEmitter streamAlarmsById(Long mid);
    Optional<AlarmDTO> createAlarm(AlarmRequest request);
}
