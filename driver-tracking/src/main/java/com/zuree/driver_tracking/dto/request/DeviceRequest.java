package com.zuree.driver_tracking.dto.request;

public record DeviceRequest(String deviceName, String deviceType, String status, Long vehicleId) {
}