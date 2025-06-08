package com.zuree.driver_tracking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class VehicleDTO {
    private Long vehicleId;
    private String manufacturer;
    private String model;
    private String vehicleType;
    private String vehicleNumber;
}
