package com.zuree.driver_tracking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DeviceDTO {
    private Long deviceId;
    private String deviceName;
    private String deviceType;
    private String status;
    private String installedAt;
    private Long vehicleId;
}
