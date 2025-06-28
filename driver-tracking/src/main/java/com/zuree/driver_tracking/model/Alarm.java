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
    private Double speed;
    private Double acceleration;
    private Double latitude;
    private Double longitude;
    private Boolean drowsiness;
    private Boolean rashDriving;
    private Boolean collision;
    private LocalDateTime alarmTime;
    private String description;
    private Boolean resolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @OneToOne(mappedBy = "alarm",   // inverse side
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = true)   // optional!
    private AlarmImage alarmImage;       // may be null

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

