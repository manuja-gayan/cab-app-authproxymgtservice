package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import com.ceyloncab.authproxymgtservice.domain.utils.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusUpdateRequest {
    @NotNull( message = "location not found for operation. This action is not allowed" )
    @Valid
    private DriverStatus status;

}
