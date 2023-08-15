package com.ceyloncab.authproxymgtservice.application.controller;

import com.ceyloncab.authproxymgtservice.domain.boundary.KafkaProducerService;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.ConfirmPickup;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.utils.PaymentType;
import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/test/com/ceyloncab/authproxymgtservice/api/test")
public class TestControler extends BaseController{
    @Autowired
    KafkaProducerService kafkaProducerService;

    @GetMapping(value = "/addData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createCustomerComplaint(HttpServletRequest request) throws Exception {

        ConfirmPickup pickup = new ConfirmPickup("6375320bee82f12693e609cd","CUSTOMER","123","1234",new Location("kalutara","6.562549804823728","79.96850453698472"),
                new Location("Moratuwa","6.808722290290415","79.90309042473322"),new ArrayList<>(), VehicleType.CAR,"nite","pc","123", PaymentType.CASH);
        kafkaProducerService.publishMsgToTopic("trip",pickup);
        CommonResponse response = new CommonResponse();
        response.setResponseHeader(new ResponseHeader("AMS1000","Success","200"));
        return getResponseEntity(HttpStatus.OK.toString(), response);


    }
}
