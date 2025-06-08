package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.VehicleDTO;
import com.zuree.driver_tracking.dto.request.VehicleRequest;
import com.zuree.driver_tracking.dto.response.ErrorResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.model.Vehicle;
import com.zuree.driver_tracking.service.VehicleService;
import com.zuree.driver_tracking.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vehicle/${api.version}")
public class VehicleController {

    final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getVehicle(@PathVariable Long id){
        Optional<VehicleDTO> vehicleOps = vehicleService.getVehicleDTOById(id);
        if (vehicleOps.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>(vehicleOps));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, AppConstants.VEHICLE_NOT_FOUND));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getVehicle(Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "vehicleId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){
        Optional<List<VehicleDTO>> vehicleOps = vehicleService.getAllVehicles(authentication, page, size, sortBy, direction);
        if (vehicleOps.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>(vehicleOps));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, AppConstants.VEHICLE_NOT_FOUND));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerVehicle(@RequestBody VehicleRequest request, Authentication authentication) {
        Optional<Vehicle> registeredDevice = vehicleService.registerVehicle(request,authentication);
        if (registeredDevice.isPresent()){
            return ResponseEntity.ok(new SuccessResponse<>("Vehicle register successfully 🚀"));
        } else {
            // Error response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(AppConstants.CODE_NOT_FOUND, "Manager not found with vehicle id 🚫"));
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> patchVehicle(@PathVariable Long id, @RequestBody VehicleDTO request) {
        Optional<Vehicle> vehicleOps = vehicleService.patchVehicle(id, request);

        return vehicleOps.<ResponseEntity<Object>>map(vehicle -> ResponseEntity.ok(new SuccessResponse<>(vehicleService.toDTO(vehicle)))).orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Vehicle Not Found 🚫")));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable Long id){
        try {
            boolean deleted = vehicleService.deleteVehicleById(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("msg","Vehicle deleted successfully 🚀"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Vehicle with ID " + id + " not found 🚫"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error","Error deleting vehicle: " + e.getMessage()));
        }
    }

}
