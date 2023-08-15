package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.TripData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripPricesRequest {
    private List<TripData>tripData;
}
