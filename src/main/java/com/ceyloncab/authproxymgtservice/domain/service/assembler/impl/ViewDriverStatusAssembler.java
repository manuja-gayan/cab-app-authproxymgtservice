package com.ceyloncab.authproxymgtservice.domain.service.assembler.impl;

import com.ceyloncab.authproxymgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.VehicleLocation.NearestVehicleLocationsDTO;
import com.ceyloncab.authproxymgtservice.domain.service.assembler.Assembler;
import org.springframework.stereotype.Service;

@Service
public class ViewDriverStatusAssembler implements Assembler<DriverStatusEntity, NearestVehicleLocationsDTO> {
    @Override
    public DriverStatusEntity fromDto(NearestVehicleLocationsDTO dto) {
        return null;
    }

    @Override
    public NearestVehicleLocationsDTO toDto(DriverStatusEntity model) {
        return new NearestVehicleLocationsDTO(model.getDriverId(),model.getStatus(),model.getVehicleType(),model.getLocation(),model.getAvgSpeed());
    }

    @Override
    public NearestVehicleLocationsDTO toDto(DriverStatusEntity model, Object object) {
        return null;
    }
}
