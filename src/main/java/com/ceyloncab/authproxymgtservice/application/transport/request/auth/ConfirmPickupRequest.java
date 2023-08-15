package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.authproxymgtservice.domain.utils.PaymentType;
import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPickupRequest {
    @NotNull(message = "userId not found for operation. This action is not allowed")
    @Valid
    private String userId;
    @NotNull(message = "channel not found for operation. This action is not allowed")
    @Valid
    private String channel;
    @NotNull(message = "uuid not found for operation. This action is not allowed")
    @Valid
    private String uuid;
    @NotNull(message = "tripId not found for operation. This action is not allowed")
    @Valid
    private String tripId;
    @NotNull(message = "start not found for operation. This action is not allowed")
    @Valid
    private Location start;
    @NotNull(message = "destination not found for operation. This action is not allowed")
    @Valid
    private Location destination;
    private List<Location> midStops;
    @NotNull(message = "vehicleType not found for operation. This action is not allowed")
    @Valid
    private VehicleType vehicleType;
    private String note;
    private String promoCode;
    private String secondaryNumber;
    @NotNull(message = "paymentType not found for operation. This action is not allowed")
    @Valid
    private PaymentType paymentType;

}
