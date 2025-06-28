package com.zuree.driver_tracking.dto.request;

public record AlarmRequest(Long deviceId, Double speed, Double acceleration,
                           Double latitude, Double longitude, Boolean drowsiness,
                           Boolean rashDriving, Boolean collision, String alarmType,
                           String description) {
}

