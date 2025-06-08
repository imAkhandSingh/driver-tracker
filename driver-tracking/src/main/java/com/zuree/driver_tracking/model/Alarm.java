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
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    private String alarmType;
    private LocalDateTime alarmTime;
    private String description;
    private Boolean resolved;

    @ManyToOne(fetch = FetchType.LAZY)
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

