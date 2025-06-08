package com.zuree.driver_tracking.util;

import com.zuree.driver_tracking.dto.VehicleDTO;
import com.zuree.driver_tracking.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleUtil {

    public void patch(Vehicle vehicle, VehicleDTO request) {
        if (request.getManufacturer() != null) vehicle.setManufacturer(request.getManufacturer());
        if (request.getVehicleNumber() != null) vehicle.setVehicleNumber(request.getVehicleNumber());
        if (request.getVehicleType() != null) vehicle.setVehicleType(request.getVehicleType());
        if (request.getModel() != null) vehicle.setModel(request.getModel());
    }
}
