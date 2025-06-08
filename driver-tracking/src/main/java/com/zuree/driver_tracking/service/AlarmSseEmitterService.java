package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.dto.request.AlarmRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmSseEmitterService {
    SseEmitter streamAlarms(Authentication authentication);
    boolean createAlarm(AlarmRequest request);
}
