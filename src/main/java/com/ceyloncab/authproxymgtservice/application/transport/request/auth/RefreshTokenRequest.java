package com.ceyloncab.authproxymgtservice.application.transport.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotNull( message = "refreshToken not found for operation. This action is not allowed" )
    @Valid
    private String refreshToken;
}
