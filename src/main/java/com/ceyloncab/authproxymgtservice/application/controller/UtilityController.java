package com.ceyloncab.authproxymgtservice.application.controller;

import com.ceyloncab.authproxymgtservice.application.transport.request.auth.DriverLocationUpdateRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.DriverStatusUpdateRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.UpdateFcnTokenRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.service.UtilityManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${base-url.context}/utility")

public class UtilityController extends BaseController{

    @Autowired
    UtilityManagementService utilityManagementService;

    @PostMapping(value = "/fcm/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateFcmToken(@Validated @RequestBody UpdateFcnTokenRequest updateFcnTokenRequest, HttpServletRequest request) throws Exception {
        CommonResponse response = utilityManagementService.updateFcmToken(updateFcnTokenRequest,request);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/update/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateDriverLocation(@Validated @RequestBody DriverLocationUpdateRequest driverLocationUpdateRequest, HttpServletRequest request) throws Exception {
        CommonResponse response = utilityManagementService.updateDriverLocation(driverLocationUpdateRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/update/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateDriverStatus(@Validated @RequestBody DriverStatusUpdateRequest driverStatusUpdateRequest, HttpServletRequest request) throws Exception {
        CommonResponse response = utilityManagementService.updateDriverStatus(driverStatusUpdateRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }
}
