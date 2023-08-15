package com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class TripPricesForAllVehicles {
    private List<TripData>tripData;
    private String userId;
    private String channel;
    private String uuid;
}
