package com.ceyloncab.authproxymgtservice.domain.boundary;

import com.ceyloncab.authproxymgtservice.application.transport.request.auth.CustomerLoginRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.login.CustomerRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    Optional<UserDetails> getCustomerProfile(String userId);

    CustomerRequest getCustomerOrCreateIfNotExist(String msisdn);

    CustomerRequest getCustomerById(String id);

    CommonResponse validateOtp(CustomerLoginRequest request);
}
