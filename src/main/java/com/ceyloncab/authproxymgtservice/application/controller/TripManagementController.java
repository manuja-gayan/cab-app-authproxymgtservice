package com.ceyloncab.authproxymgtservice.application.controller;


import com.ceyloncab.authproxymgtservice.application.transport.request.auth.VehicleRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.VehicleLocation.NearestVehicleLocationsDTO;
import com.ceyloncab.authproxymgtservice.domain.service.TripManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("${base-url.context}/trip-mgt")
public class TripManagementController extends BaseController{

    @Autowired
    TripManagementService tripManagementService;

   @PostMapping(value = "/get-vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNearestVehicleLocationsByType(@RequestBody(required = true) VehicleRequest vehicleRequest, HttpServletRequest request) throws Exception {
       CommonResponse<List<NearestVehicleLocationsDTO>> response = tripManagementService.getNearestVehicleLocationsByType(vehicleRequest);
       return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

}
