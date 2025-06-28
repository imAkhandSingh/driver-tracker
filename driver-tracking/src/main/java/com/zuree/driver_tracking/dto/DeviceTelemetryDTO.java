package com.zuree.driver_tracking.dto;

import lombok.Builder;

@Builder
public record DeviceTelemetryDTO(Long telemetryId, Long deviceId , Double speed, Double acceleration,
                                 Double latitude, Double longitude, Boolean drowsiness,
                                 Boolean rashDriving, Boolean collision,
                                 String logTime) {
}
