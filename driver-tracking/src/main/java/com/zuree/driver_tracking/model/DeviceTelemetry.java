package com.zuree.driver_tracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "device_telemetry")
public class DeviceTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telemetryId;

    private String status;
    private Double speed;
    private Double acceleration;
    private Double latitude;
    private Double longitude;
    private Boolean drowsiness;
    private Boolean rashDriving;
    private Boolean collision;

    private LocalDateTime logTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

}

