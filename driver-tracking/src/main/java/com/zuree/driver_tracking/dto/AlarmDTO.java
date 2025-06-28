package com.zuree.driver_tracking.dto;

public record AlarmDTO(Long deviceId , Long alarmId, Double speed, Double acceleration,
        Double latitude, Double longitude, Boolean drowsiness,
        Boolean rashDriving, Boolean collision,
        String alarmType, String description, String previewUrl, String downloadUrl, String alarmTime) {
}
