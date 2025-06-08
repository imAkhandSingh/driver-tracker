package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.ManagerDTO;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/${api.base-version}")
public class AuthController {

    private final ManagerRepository managerRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthController(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String rawPassword = body.get("password");
        String name = body.get("name");
        String phoneNumber = body.get("phoneNumber");

        // Error response
        if (managerRepository.findByEmail(email).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "User already exists with this email!"));
        }

        Manager manager = new Manager();
        manager.setEmail(email);
        manager.setPassword(passwordEncoder.encode(rawPassword));
        manager.setName(name);
        manager.setPhoneNumber(phoneNumber);
        managerRepository.save(manager);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("msg", "User registered successfully!"));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<Manager> userOpt = managerRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Manager user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                ManagerDTO managerDTO = ManagerDTO.builder().id(user.getManagerId()).name(user.getName()).email(user.getEmail()).phoneNumber(user.getPhoneNumber()).build();
                return ResponseEntity.ok(managerDTO);
            }
        }

        // Error response
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password."));
    }
}

