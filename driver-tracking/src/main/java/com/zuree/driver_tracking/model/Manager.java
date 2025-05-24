package com.zuree.driver_tracking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@Entity
@Table(name = "manager")
public class Manager {

    public Manager() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    private String name;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Device> devices;

}

