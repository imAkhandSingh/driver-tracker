package com.zuree.driver_tracking.dto.request;

public record VehicleRequest(String manufacturer, String vehicleType, String model, String vehicleNumber) {
}