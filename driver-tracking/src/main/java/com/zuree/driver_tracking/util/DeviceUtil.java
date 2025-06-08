package com.zuree.driver_tracking.util;

import com.zuree.driver_tracking.dto.DeviceDTO;
import com.zuree.driver_tracking.model.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceUtil {

    public void patch(Device device, DeviceDTO request) {
        if (request.getDeviceName() != null) device.setDeviceName(request.getDeviceName());
        if (request.getStatus() != null) device.setStatus(request.getStatus());
        if (request.getDeviceType() != null) device.setDeviceType(request.getDeviceType());
    }
}
