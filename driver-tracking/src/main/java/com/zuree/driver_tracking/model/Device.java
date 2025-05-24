package com.zuree.driver_tracking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    private String deviceName;
    private String deviceType;
    private String status;
    private LocalDateTime installedAt;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<Alarm> alarms;

    @Override
    public String toString() {
        return "Device{id=" + deviceId + "}";
    }
}

