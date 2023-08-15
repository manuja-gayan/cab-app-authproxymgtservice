package com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndTrip extends UpDateStatusOnTrip{
    private Double distance;
    private Double waitingTime = 0.0;
    private String tripTime;
    private Double avgSpeed;
}
