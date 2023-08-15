package com.ceyloncab.authproxymgtservice.domain.service;

import com.ceyloncab.authproxymgtservice.application.auth.JwtService;
import com.ceyloncab.authproxymgtservice.application.auth.TokenType;
import com.ceyloncab.authproxymgtservice.application.auth.dto.AuthResponse;
import com.ceyloncab.authproxymgtservice.application.auth.dto.UserData;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.CustomerLoginRequest;
import com.ceyloncab.authproxymgtservice.domain.boundary.UserService;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.login.CustomerRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.exception.DomainException;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.UserRole;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerManagementService {
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    public CommonResponse<AuthResponse>performLogin(CustomerLoginRequest request){
        CommonResponse<AuthResponse> response= new CommonResponse<>();
        try {
            CommonResponse otpResponse = userService.validateOtp(request);
            if("OTP1001".equalsIgnoreCase(otpResponse.getResponseHeader().getCode())){
                CustomerRequest customer = userService.getCustomerOrCreateIfNotExist(request.getMsisdn());
                //mapping to user object
                UserData userData = new UserData(customer.getUserId(), customer.getMsisdn(), UserRole.CUSTOMER, customer.getMsisdn());
                String sessionId = RandomString.make(20);
                AuthResponse authResponse = new AuthResponse(customer.getUserId(),UserRole.CUSTOMER,customer.getMsisdn(),
                        jwtService.generateToken(userData, TokenType.ACCESS_TOKEN.name(),sessionId),
                        jwtService.generateToken(userData, TokenType.REFRESH_TOKEN.name(),sessionId));
                authResponse.setIsFirstLogin(customer.getIsFirstLogin());

                ResponseHeader header = new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS);
                response.setData(authResponse);
                response.setResponseHeader(header);
                jwtService.saveUserToken(userData,authResponse.getAccessToken(),authResponse.getRefreshToken(),sessionId);
                return response;
            }else {
                throw new DomainException("APMS3000","Otp not matched","400");
            }
        } catch (Exception e) {
            throw new DomainException(Constants.ResponseData.COMMON_FAIL);
        }
    }

}
