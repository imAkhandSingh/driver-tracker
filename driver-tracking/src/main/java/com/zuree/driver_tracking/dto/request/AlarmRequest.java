package com.zuree.driver_tracking.dto.request;

public record AlarmRequest(Long deviceId ,String alarmType ,String description) {
}
