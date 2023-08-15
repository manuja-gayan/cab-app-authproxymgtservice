package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationUpdateRequest {
    @NotNull( message = "location not found for operation. This action is not allowed" )
    @Valid
    private List<Double> location;
    @NotNull( message = "avgSpeed not found for operation. This action is not allowed" )
    @Valid
    private Double avgSpeed;
}
