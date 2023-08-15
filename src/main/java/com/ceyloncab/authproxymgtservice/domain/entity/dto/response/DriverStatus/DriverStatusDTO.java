package com.ceyloncab.authproxymgtservice.domain.entity.dto.response.DriverStatus;

import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusDTO{
    private String id;
    private String status;
    private String driverId;
    private VehicleType vehicleType;
    private GeoJsonPoint location;
    private Double avgSpeed;

}
