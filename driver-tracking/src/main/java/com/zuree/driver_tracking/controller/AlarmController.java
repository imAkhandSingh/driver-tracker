package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.model.Device;
import com.zuree.driver_tracking.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequestMapping("/alarms/${api.base-version}")
public class AlarmController {

    AlarmRepository alarmRepository;

    @Autowired
    public AlarmController(AlarmRepository alarmRepository){
        this. alarmRepository = alarmRepository;
    }

    @RateLimiter(name = "apiRateLimiter")
    @PostMapping("/add")
    public ResponseEntity<Object> setAlarm(@RequestBody Map<String, String> body){
        String alarmType = body.get("alarmType");
        String description = body.get("description");
        Long deviceId = Long.parseLong(body.get("deviceId"));
        Device device = new Device();
        device.setDeviceId(deviceId);

        Alarm alarm = Alarm.builder().alarmType(alarmType).description(description)
                .alarmTime(LocalDateTime.now(ZoneId.of("Asia/Kolkata")))
                .device(device)
                .resolved(false)
                .build();

        alarmRepository.save(alarm);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("Alarm successfully added!"));

    }
}
