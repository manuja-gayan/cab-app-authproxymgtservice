package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoginRequest {
    @NotNull( message = "msisdn not found for operation. This action is not allowed" )
    @Valid
    private String msisdn;
    @NotNull( message = "otp not found for operation. This action is not allowed" )
    @Valid
    private String otp;
}
