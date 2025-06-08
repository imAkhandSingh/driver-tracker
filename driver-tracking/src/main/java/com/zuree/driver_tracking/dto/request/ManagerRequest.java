package com.zuree.driver_tracking.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ManagerRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Must be valid email address")
    private String email;
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}",message = "Mobile number must have exactly 10 digits")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
