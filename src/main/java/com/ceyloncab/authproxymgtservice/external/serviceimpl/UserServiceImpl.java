package com.ceyloncab.authproxymgtservice.external.serviceimpl;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.CustomerLoginRequest;
import com.ceyloncab.authproxymgtservice.domain.boundary.UserService;
import com.ceyloncab.authproxymgtservice.domain.entity.AdminEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.login.CustomerRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.login.OtpValidateRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.Status;
import com.ceyloncab.authproxymgtservice.domain.utils.UserRole;
import com.ceyloncab.authproxymgtservice.external.exception.ExternalException;
import com.ceyloncab.authproxymgtservice.external.repository.AdminRepository;
import com.ceyloncab.authproxymgtservice.external.repository.DriverRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

/**;
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final DriverRepository driverRepository;

    private final AdminRepository adminRepository;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Value("${url.usermgt.get-or-create-customer}")
    private String createOrGetCustomerUrl;

    @Value("${url.usermgt.get-customer}")
    private String getCustomerUrl;

    @Value("${url.otpservicemgt.validate-otp}")
    private String otpValidateUrl;

    @Autowired
    public UserServiceImpl(DriverRepository driverRepository, PasswordEncoder passwordEncoder,
                           AdminRepository adminRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    //get user
    @Override
    public Optional<UserDetails> getCustomerProfile(String userUUID){

        UserRole role = UserRole.valueOf(MDC.get(AopConstants.CHANNEL));
        switch (role){
            case ADMIN:
                Optional<AdminEntity> admin = adminRepository.findByEmail(userUUID);
                //TODO: once pw encoding at user store time implemented we just need to return the pw(without encode part)
                return admin.map(adminEntity -> new User(adminEntity.getEmail(),passwordEncoder.encode(adminEntity.getPassword()),Collections.singleton(new SimpleGrantedAuthority(role.name()))));
            case DRIVER:
                Optional<DriverEntity> driver = driverRepository.findByMsisdn(userUUID);
                //TODO: once pw encoding at user store time implemented we just need to return the pw(without encode part)
                return driver.map(driverEntity -> new User(driverEntity.getMsisdn(),passwordEncoder.encode(driverEntity.getPassword()),Collections.singleton(new SimpleGrantedAuthority(role.name()))));
            default:
                break;
        }
        return Optional.empty();
    }

    @Override
    public CustomerRequest getCustomerOrCreateIfNotExist(String msisdn){
        CustomerRequest request = new CustomerRequest(null,msisdn,"UNSPECIFIED","UNSPECIFIED","UNSPECIFIED",null,"UNSPECIFIED", Status.ACTIVE,null);
        try {
            // Create header and set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.put("Channel", Collections.singletonList("CUSTOMER"));
            // Request entity is created with request headers
            HttpEntity<?> requestEntity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(createOrGetCustomerUrl, HttpMethod.POST,requestEntity,String.class);
            if(HttpStatus.CREATED.equals(response.getStatusCode())){
                CommonResponse<CustomerRequest> customerResp = objectMapper.readValue(response.getBody(), new TypeReference<CommonResponse<CustomerRequest>>() {});
                return customerResp.getData();
            }else {
                throw new ExternalException(Constants.ResponseData.CUSTOMER_FETCH_FAILED);
            }
        }catch (Exception ex){
            log.error("Error occurred while getting customer profile.Error:{}",ex.getMessage(),ex);
            throw new ExternalException(Constants.ResponseData.CUSTOMER_FETCH_FAILED);
        }
    }

    @Override
    public CustomerRequest getCustomerById(String id){

        try {
            // Create header and set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.put("Channel", Collections.singletonList("CUSTOMER"));
            headers.put("UserId", Collections.singletonList(id));
            // Request entity is created with request headers
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(getCustomerUrl, HttpMethod.GET,requestEntity,String.class);
            if(HttpStatus.OK.equals(response.getStatusCode())){
                CommonResponse<CustomerRequest> customerResp = objectMapper.readValue(response.getBody(), new TypeReference<CommonResponse<CustomerRequest>>() {});
                return customerResp.getData();
            }else {
                throw new ExternalException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
            }
        }catch (Exception ex){
            log.error("Error occurred while getting customer profile.Error:{}",ex.getMessage(),ex);
            throw new ExternalException(Constants.ResponseData.CUSTOMER_FETCH_FAILED);
        }
    }

    @Override
    public CommonResponse validateOtp(CustomerLoginRequest request) {
        OtpValidateRequest otpRequest = new OtpValidateRequest(request.getOtp());
        otpRequest.setMsisdn(request.getMsisdn());
        otpRequest.setAction("LOGIN");
        try {
            // Create header and set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.put("Channel", Collections.singletonList("CUSTOMER"));
            // Request entity is created with request headers
            HttpEntity<?> requestEntity = new HttpEntity<>(otpRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(otpValidateUrl, HttpMethod.POST,requestEntity,String.class);
            if(HttpStatus.OK.equals(response.getStatusCode())){
                CommonResponse otpResp = objectMapper.readValue(response.getBody(), new TypeReference<CommonResponse>() {});
                return otpResp;
            }else {
                throw new ExternalException(Constants.ResponseData.CUSTOMER_FETCH_FAILED);
            }
        }catch (HttpClientErrorException ce){
            log.error("Client error occurred while validating otp.Error:{}",ce.getMessage(),ce);
            throw ce;
        } catch (Exception ex){
            log.error("Error occurred while validating otp.Error:{}",ex.getMessage(),ex);
            throw new ExternalException(Constants.ResponseData.COMMON_FAIL);
        }
    }


}
