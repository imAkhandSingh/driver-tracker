package com.zuree.driver_tracking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record AlarmManagerDTO(
        Long deviceId,
        Long alarmId,
        String alarmType,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Timestamp alarmTime
) {}

