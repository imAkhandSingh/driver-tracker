package com.zuree.driver_tracking.service;

import com.nimbusds.jose.JOSEException;
import com.zuree.driver_tracking.dto.ManagerDTO;
import com.zuree.driver_tracking.dto.request.LoginRequest;
import com.zuree.driver_tracking.dto.request.ManagerRequest;

import java.util.Map;

public interface ManagerService {
    ManagerDTO createManager(ManagerRequest request);
    boolean isPresent(ManagerRequest request);
    Map<String, Object> generateMangerToken(LoginRequest request) throws JOSEException;
}
