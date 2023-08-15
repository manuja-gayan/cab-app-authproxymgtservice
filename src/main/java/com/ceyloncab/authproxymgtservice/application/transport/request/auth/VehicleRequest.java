package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.VehicleLocation;
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
public class VehicleRequest {
    @NotNull( message = "queryType not found for operation. This action is not allowed" )
    @Valid
    private String queryType;
    @NotNull( message = "location not found for operation. This action is not allowed" )
    @Valid
    private List<Double> location;
    @NotNull( message = "queryParam not found for operation. This action is not allowed" )
    @Valid
    private String queryParam;
}
