package com.zuree.driver_tracking.dto;

public record AlarmDTO(Long deviceId , Long alarmId, String alarmType, String description, String alarmTime) {
}
