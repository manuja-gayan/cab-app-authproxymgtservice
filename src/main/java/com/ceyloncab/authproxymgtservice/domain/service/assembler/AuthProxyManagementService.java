package com.ceyloncab.authproxymgtservice.domain.service.assembler;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.ConfirmPickupRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.EndTripRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.TripPricesRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.TripStatusUpdateRequest;
import com.ceyloncab.authproxymgtservice.domain.boundary.KafkaProducerService;
import com.ceyloncab.authproxymgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.*;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.exception.DomainException;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.external.repository.CustomerRepository;
import com.ceyloncab.authproxymgtservice.external.repository.DriverRepository;
import com.ceyloncab.authproxymgtservice.external.serviceimpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class AuthProxyManagementService {

    @Autowired
    KafkaProducerService kafkaProducerService;
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DriverRepository driverRepository;

    public CommonResponse confirmPickup(ConfirmPickupRequest request){
        CommonResponse response = new CommonResponse<>();

        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });

        ConfirmPickup confirmPickup = new ConfirmPickup(customer.getUserId(),MDC.get(AopConstants.CHANNEL),MDC.get(AopConstants.UUID), getUniquedeltaId().toString(),
                new Location(request.getStart().getName(), request.getStart().getLatitude(), request.getStart().getLongitude()),
                new Location(request.getDestination().getName(), request.getDestination().getLatitude(), request.getDestination().getLongitude()),
                null, request.getVehicleType(), request.getNote(), request.getPromoCode(), request.getSecondaryNumber(), request.getPaymentType());
        kafkaProducerService.publishMsgToTopic("trip", confirmPickup);

        response.setData(confirmPickup.getTripId());
        return response;

    }

    public CommonResponse getTripPricesForAllVehicles(TripPricesRequest request) {
        CommonResponse<Object> response = new CommonResponse<>();

        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });

        TripPricesForAllVehicles tripPricesForAllVehicles = new TripPricesForAllVehicles();
        tripPricesForAllVehicles.setTripData(request.getTripData());
        tripPricesForAllVehicles.setUserId(customer.getUserId());
        tripPricesForAllVehicles.setChannel( AopConstants.CHANNEL);
        tripPricesForAllVehicles.setUuid(AopConstants.UUID);
        kafkaProducerService.publishMsgToTopic("trip", tripPricesForAllVehicles);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS));
        return response;

    }

    public CommonResponse driverAcceptTrip(TripStatusUpdateRequest request) {

        CommonResponse response = new CommonResponse<>();
        DriverEntity driver = driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });
        UpDateStatusOnTrip driverAcceptTrip= new UpDateStatusOnTrip(driver.getUserId(),AopConstants.CHANNEL, AopConstants.UUID,
                request.getStatus(), request.getTripId());
        kafkaProducerService.publishMsgToTopic("trip", driverAcceptTrip);
        response.setData(driverAcceptTrip.getTripId());
        return response;

    }

    public CommonResponse driverCancelTrip(TripStatusUpdateRequest request) {

        CommonResponse response = new CommonResponse<>();
        DriverEntity driver = driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });
        UpDateStatusOnTrip driverCancelTrip= new UpDateStatusOnTrip(driver.getUserId(),AopConstants.CHANNEL, AopConstants.UUID,
                request.getStatus(), request.getTripId());
        kafkaProducerService.publishMsgToTopic("trip", driverCancelTrip);
        response.setData(driverCancelTrip.getTripId());
        return response;

    }

    public CommonResponse customerCancelTrip(TripStatusUpdateRequest request)  {

        CommonResponse response = new CommonResponse<>();
        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });
        UpDateStatusOnTrip customerCancelTrip= new UpDateStatusOnTrip(customer.getUserId(),AopConstants.CHANNEL, AopConstants.UUID,
                request.getStatus(), request.getTripId());
        kafkaProducerService.publishMsgToTopic("trip", customerCancelTrip);
        response.setData(customerCancelTrip.getTripId());
        return response;

    }

    public CommonResponse endTrip(EndTripRequest request) {

        CommonResponse response = new CommonResponse<>();
        DriverEntity driver = driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });
        EndTrip driverCompletedTrip= new EndTrip();
        driverCompletedTrip.setTripId(request.getTripId());
        driverCompletedTrip.setStatus(request.getStatus());
        driverCompletedTrip.setUserId(driver.getUserId());
        driverCompletedTrip.setChannel(MDC.get(AopConstants.CHANNEL));
        driverCompletedTrip.setUuid(MDC.get(AopConstants.UUID));
        driverCompletedTrip.setDistance(request.getDistance());
        driverCompletedTrip.setWaitingTime(request.getWaitingTime());
        driverCompletedTrip.setTripTime(request.getTripTime());
        driverCompletedTrip.setAvgSpeed(request.getAvgSpeed());
        kafkaProducerService.publishMsgToTopic("trip", driverCompletedTrip);
        response.setData(driverCompletedTrip.getTripId());
        return response;

    }

    public CommonResponse startTrip(TripStatusUpdateRequest request){

        CommonResponse response = new CommonResponse<>();
        DriverEntity driver = driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });
        UpDateStatusOnTrip driverStartedTrip= new UpDateStatusOnTrip(driver.getUserId(),AopConstants.CHANNEL, AopConstants.UUID,
                request.getStatus(), request.getTripId());
        kafkaProducerService.publishMsgToTopic("trip", driverStartedTrip);
        response.setData(driverStartedTrip.getTripId());
        return response;

    }

    public Long getUniquedeltaId() {
        final Long LIMIT = 10000L;
        final Long deltaId = Long.parseLong(Long.toString(Long.parseLong((java.time.LocalDate.now()
                .format(DateTimeFormatter
                        .ofPattern("yyMMdd")))))
                .concat(Long.toString(System.currentTimeMillis() % LIMIT)));
        System.out.println("deltaId" + deltaId);
        return deltaId;

    }
}
