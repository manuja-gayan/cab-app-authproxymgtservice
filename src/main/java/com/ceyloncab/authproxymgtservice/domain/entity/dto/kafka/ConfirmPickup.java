package com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka;

import com.ceyloncab.authproxymgtservice.domain.utils.PaymentType;
import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmPickup implements Serializable {
    private String userId;
    private String channel;
    private String uuid;
    private String tripId;
    private Location start;
    private Location destination;
    private List<Location> midStops;
    private VehicleType vehicleType;
    private String note;
    private String promoCode;
    private String secondaryNumber;
    private PaymentType paymentType;
}
