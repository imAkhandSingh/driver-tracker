package com.zuree.driver_tracking.service.impl;

import com.nimbusds.jose.JOSEException;
import com.zuree.driver_tracking.dto.ManagerDTO;
import com.zuree.driver_tracking.dto.request.LoginRequest;
import com.zuree.driver_tracking.dto.request.ManagerRequest;
import com.zuree.driver_tracking.model.Manager;
import com.zuree.driver_tracking.repository.ManagerRepository;
import com.zuree.driver_tracking.service.ManagerService;
import com.zuree.driver_tracking.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public ManagerDTO createManager(ManagerRequest request) {
        Manager manager = new Manager();
        manager.setEmail(request.getEmail());
        manager.setPassword(passwordEncoder.encode(request.getPassword()));
        manager.setName(request.getName());
        manager.setPhoneNumber(request.getPhoneNumber());
        managerRepository.save(manager);
        return ManagerDTO.builder()
                .id(manager.getManagerId())
                .email(manager.getEmail())
                .name(manager.getName())
                .phoneNumber(manager.getPhoneNumber())
                .build();
    }

    @Override
    public boolean isPresent(ManagerRequest request) {
        return managerRepository.findByEmail(request.getEmail()).isPresent();
    }

    @Override
    public Map<String, Object> generateMangerToken(LoginRequest request) throws JOSEException {
        String email = request.email();
        Optional<Manager> userOpt = managerRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Manager user = userOpt.get();
            String userEmail = user.getEmail();
            String userPassword = user.getPassword();
            if (userEmail.equals(email) && passwordEncoder.matches(request.password(), userPassword)) {
                ManagerDTO managerDTO = ManagerDTO.builder().id(user.getManagerId()).name(user.getName()).email(user.getEmail()).phoneNumber(user.getPhoneNumber()).build();
                String token = JwtUtil.generateToken(email);
                Map<String, Object> map = new HashMap<>();
                map.put("manager", managerDTO);
                map.put("token", token);
                return map;
            }
        }
        return Map.of();
    }
}
