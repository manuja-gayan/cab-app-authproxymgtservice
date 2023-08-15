package com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka;

import com.ceyloncab.authproxymgtservice.domain.utils.TripStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpDateStatusOnTrip {
    private String userId;
    private String channel;
    private String uuid;
    private String status;
    private String tripId;
}
