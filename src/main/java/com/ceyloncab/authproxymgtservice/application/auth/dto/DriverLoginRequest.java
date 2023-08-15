package com.ceyloncab.authproxymgtservice.application.auth.dto;

import lombok.Data;

@Data
public class DriverLoginRequest {
    private String msisdn;
    private String password;
}
