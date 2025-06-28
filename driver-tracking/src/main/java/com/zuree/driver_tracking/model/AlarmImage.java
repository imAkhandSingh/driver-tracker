package com.zuree.driver_tracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "alarm_image")
public class AlarmImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    private String filename;        // original name
    private String storedName;      // UUID_xxx.jpg on disk
    private String contentType;
    private Long   size;
    private String path;            // e.g. uploads/UUID_xxx.jpg
    private LocalDateTime uploadedAt;

    @OneToOne
    @JoinColumn(name = "alarm_id",  // FK column
            nullable = true,     // <— allows null
            unique   = true)
    private Alarm alarm;
    /* getters & setters omitted for brevity */
}

