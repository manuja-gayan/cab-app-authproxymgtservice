package com.ceyloncab.authproxymgtservice.domain.service;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.VehicleRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.VehicleLocation.NearestVehicleLocationsDTO;
import com.ceyloncab.authproxymgtservice.domain.exception.DomainException;
import com.ceyloncab.authproxymgtservice.domain.service.assembler.impl.ViewDriverStatusAssembler;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import com.ceyloncab.authproxymgtservice.external.repository.CustomerRepository;
import com.ceyloncab.authproxymgtservice.external.repository.DriverStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TripManagementService {

    @Value(value = "1")
    private Integer searchRange;


    @Autowired
    private DriverStatusRepository driverStatusRepository;

    @Autowired
    private ViewDriverStatusAssembler viewDriverStatusAssembler;

    @Autowired
    private CustomerRepository customerRepository;

    public CommonResponse<List<NearestVehicleLocationsDTO>> getNearestVehicleLocationsByType(VehicleRequest vehicleRequest){

        CommonResponse<List<NearestVehicleLocationsDTO>> response = new CommonResponse<>();
        List<NearestVehicleLocationsDTO> vehicleList = new ArrayList<>();

        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });

        if ("BY_TYPE".equalsIgnoreCase(vehicleRequest.getQueryType())) {
            List<DriverStatusEntity> driverStatusEntityList = driverStatusRepository.findByVehicleTypeAndStatusAndLocationNear(VehicleType.valueOf(vehicleRequest.getQueryParam()),"IDLE",new GeoJsonPoint(vehicleRequest.getLocation().get(0),vehicleRequest.getLocation().get(01)),new Distance(searchRange, Metrics.KILOMETERS));
            for (DriverStatusEntity driverStatusEntity : driverStatusEntityList) {
                vehicleList.add(viewDriverStatusAssembler.toDto(driverStatusEntity));
            }
        } else if ("BY_ID".equalsIgnoreCase(vehicleRequest.getQueryType())) {
            List<DriverStatusEntity> driverStatusEntityList = driverStatusRepository.findByDriverId(vehicleRequest.getQueryParam());

            for ( DriverStatusEntity driverStatusEntity : driverStatusEntityList) {
                vehicleList.add(viewDriverStatusAssembler.toDto(driverStatusEntity));
            }

        } else {
            log.error("QueryType not found");
            throw new DomainException(Constants.ResponseData.COMMON_FAIL);
        }

        response.setData(vehicleList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS));
        return response;


    }
}

