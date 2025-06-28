package com.zuree.driver_tracking.controller.jwt;

import com.nimbusds.jose.JOSEException;
import com.zuree.driver_tracking.dto.ManagerDTO;
import com.zuree.driver_tracking.dto.request.LoginRequest;
import com.zuree.driver_tracking.dto.request.ManagerRequest;
import com.zuree.driver_tracking.dto.response.BaseResponse;
import com.zuree.driver_tracking.dto.response.ErrorResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/${api.version}")
public class AuthManagerJWTController {
    private final ManagerService managerService;

    public AuthManagerJWTController(ManagerService managerService) {
        this.managerService = managerService;
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest loginRequest) throws JOSEException {
        Map<String, Object> map = managerService.generateMangerToken(loginRequest);
        if (!map.isEmpty()) {
            // Successful response
            return ResponseEntity.ok(new SuccessResponse<>(managerService.generateMangerToken(loginRequest)));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "Invalid email or password!"));
        }
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@Valid @RequestBody ManagerRequest request) {
        // Conflict response
        if (managerService.isPresent(request)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(409, "User already exists with this email!"));
        }
        ManagerDTO data = managerService.createManager(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponse<>(data));
    }
}

