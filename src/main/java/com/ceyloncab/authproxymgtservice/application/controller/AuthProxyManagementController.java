package com.ceyloncab.authproxymgtservice.application.controller;

import com.ceyloncab.authproxymgtservice.application.transport.request.auth.ConfirmPickupRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.EndTripRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.TripPricesRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.TripStatusUpdateRequest;
import com.ceyloncab.authproxymgtservice.domain.boundary.KafkaProducerService;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.ConfirmPickup;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.service.assembler.AuthProxyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${base-url.context}/trip")
public class AuthProxyManagementController extends BaseController{
    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    AuthProxyManagementService authProxyManagementService;

    @PostMapping(value = "/confirmPickup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> confirmPickup( @RequestBody(required = true) ConfirmPickupRequest confirmPickupRequest, HttpServletRequest request)  {
        CommonResponse<ConfirmPickup> response = authProxyManagementService.confirmPickup(confirmPickupRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000","Successfully Submitted","200"));
        return getResponseEntity("200", response);

       /*ConfirmPickup confirmPickup = new ConfirmPickup(createRequest.getUserId(), createRequest.getChannel(), createRequest.getUuid(), createRequest.getTripId(),
                new Location(createLocationReq.getName(), createLocationReq.getLatitude(),createLocationReq.getLongitude()),
                new Location(createLocationReq.getName(), createLocationReq.getLatitude(), createLocationReq.getLongitude()), null,
                createRequest.getVehicleType(),createRequest.getNote(), createRequest.getPromoCode(),createRequest.getPaymentType());
         kafkaProducerService.publishMsgToTopic("trip",confirmPickup);
        System.out.println(confirmPickup.getDestination().getName());*/


    }

    @PostMapping(value = "/pricesForAllVehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> pricesF0rAllVehicles(@RequestBody(required = true)TripPricesRequest tripPricesRequest, HttpServletRequest request)  {
        CommonResponse response = authProxyManagementService.getTripPricesForAllVehicles(tripPricesRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000", "Success", "200"));
        return getResponseEntity("200", response);
    }

    @PostMapping(value = "/driverAcceptTrip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> driverAcceptTrip(@RequestBody(required = true)TripStatusUpdateRequest tripStatusUpdateRequest, HttpServletRequest request) {
        CommonResponse response = authProxyManagementService.driverAcceptTrip(tripStatusUpdateRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000", "Successfully Submitted", "200"));
        return getResponseEntity("200", response);
    }

    @PostMapping(value = "/driverCancelTrip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> driverCancelTrip(@RequestBody(required = true)TripStatusUpdateRequest tripStatusUpdateRequest, HttpServletRequest request)  {
        CommonResponse response = authProxyManagementService.driverCancelTrip(tripStatusUpdateRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000", "Successfully Submitted", "200"));
        return getResponseEntity("200", response);
    }

    @PostMapping(value = "/customerCancelTrip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> customerCancelTrip(@RequestBody(required = true)TripStatusUpdateRequest tripStatusUpdateRequest, HttpServletRequest request) throws Exception {
        CommonResponse response = authProxyManagementService.customerCancelTrip(tripStatusUpdateRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000", "Successfully Submitted", "200"));
        return getResponseEntity("200", response);
    }

    @PostMapping(value = "/endTrip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> endTrip(@RequestBody(required = true) EndTripRequest endTripRequest, HttpServletRequest request)  {
        CommonResponse  response = authProxyManagementService.endTrip(endTripRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000", "Successfully Submitted", "200"));
        return getResponseEntity("200", response);
    }

    @PostMapping(value = "/startTrip", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> startTrip(@RequestBody(required = true)TripStatusUpdateRequest tripStatusUpdateRequest, HttpServletRequest request)  {
        CommonResponse  response = authProxyManagementService.startTrip(tripStatusUpdateRequest);
        response.setResponseHeader(new ResponseHeader("TMS0000", "Successfully Submitted", "200"));
        return getResponseEntity("200", response);
    }
}
