package com.zuree.driver_tracking.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Entity
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    private String alarmType;
    private LocalDateTime alarmTime;
    private String description;
    private Boolean resolved;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Override
    public String toString() {
        return "Alarm{" +
                "alarmId=" + alarmId +
                ", alarmType='" + alarmType + '\'' +
                ", alarmTime=" + alarmTime +
                ", description='" + description + '\'' +
                ", resolved=" + resolved +
                ", device=" + device.getDeviceId() +
                '}';
    }
}

