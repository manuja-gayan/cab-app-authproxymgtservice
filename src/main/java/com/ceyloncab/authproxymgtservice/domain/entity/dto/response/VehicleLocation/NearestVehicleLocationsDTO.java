package com.ceyloncab.authproxymgtservice.domain.entity.dto.response.VehicleLocation;

import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.VehicleLocation;
import com.ceyloncab.authproxymgtservice.domain.utils.DriverStatus;
import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearestVehicleLocationsDTO {
    private String userId;
    private String status;
    private VehicleType vehicleType;
    private GeoJsonPoint location;
    private double avgSpeed;

}
