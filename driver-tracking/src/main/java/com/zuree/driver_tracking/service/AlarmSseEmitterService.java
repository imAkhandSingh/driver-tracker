package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.AlarmDTO;
import com.zuree.driver_tracking.dto.VehicleDTO;
import com.zuree.driver_tracking.dto.request.AlarmRequest;
import com.zuree.driver_tracking.model.Alarm;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

public interface AlarmSseEmitterService {
    SseEmitter streamAlarms(Authentication authentication);
    boolean createAlarm(AlarmRequest request);
}
