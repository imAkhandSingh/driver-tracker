package com.zuree.driver_tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerDTO {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
}
