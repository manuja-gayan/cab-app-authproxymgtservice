package com.ceyloncab.authproxymgtservice.application.controller;

import com.ceyloncab.authproxymgtservice.application.auth.JwtService;
import com.ceyloncab.authproxymgtservice.application.auth.dto.AuthResponse;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.CustomerLoginRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.RefreshTokenRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.service.CustomerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *  this class use to handle authentication related functionalities.
 *  NOTE: Any of this controller related urls will not authenticate by authentication filters
 */
@CrossOrigin
@RestController
@RequestMapping("${base-url.context}/auth")
public class AuthenticationController extends BaseController{
    @Autowired
    JwtService jwtService;

    @Autowired
    CustomerManagementService customerManagementService;

    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refreshToken(@Validated @RequestBody(required = true) RefreshTokenRequest refreshTokenRequest, HttpServletRequest request){

        CommonResponse<AuthResponse> response = jwtService.getRefreshToken(refreshTokenRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

    @PostMapping(value = "/customer/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> customerLogin(@Validated @RequestBody(required = true) CustomerLoginRequest loginRequest, HttpServletRequest request){

        CommonResponse<AuthResponse> response = customerManagementService.performLogin(loginRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }
}
