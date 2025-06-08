package com.zuree.driver_tracking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String vehicleNumber;
    private String vehicleType;
    private String model;
    private String manufacturer;

    @OneToOne(mappedBy = "vehicle")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Override
    public String toString() {
        return "Vehicle{id=" + vehicleId + ", deviceId=" + (device != null ? device.getDeviceId() : null) + "}";
    }
}

