package com.ceyloncab.authproxymgtservice.domain.service;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.DriverLocationUpdateRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.DriverStatusUpdateRequest;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.UpdateFcnTokenRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.UserTokenEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.DriverStatus.DriverStatusDTO;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.exception.DomainException;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.DriverStatus;
import com.ceyloncab.authproxymgtservice.external.repository.DriverRepository;
import com.ceyloncab.authproxymgtservice.external.repository.DriverStatusRepository;
import com.ceyloncab.authproxymgtservice.external.repository.UserTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UtilityManagementService {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    DriverStatusRepository driverStatusRepository;

    @Autowired
    UserTokenRepository userTokenRepository;

public CommonResponse  updateDriverLocation(DriverLocationUpdateRequest driverLocationUpdateRequest){
    CommonResponse<Object> response = new CommonResponse<>();

    DriverEntity driverEntity = driverRepository.findById(MDC.get(AopConstants.MDC_USERID)).orElseThrow(() -> {
        log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
        return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
    });

    try{
        String userId = driverEntity.getUserId();
        Optional<DriverStatusEntity> byDriversId = driverStatusRepository.findDriverStatusEntityByDriverId(userId);
        byDriversId.get().setLocation(new GeoJsonPoint(driverLocationUpdateRequest.getLocation().get(0),driverLocationUpdateRequest.getLocation().get(1)));
        byDriversId.get().setAvgSpeed(driverLocationUpdateRequest.getAvgSpeed());
        driverStatusRepository.save(byDriversId.get());
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.UPDATE_SUCCESS));

    }catch (Exception ex) {
        log.error("Error occurred in updateCustomerProfile.Error:{}", ex.getMessage());
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
        return response;
    }

    response.setResponseHeader(new ResponseHeader(Constants.ResponseData.UPDATE_SUCCESS));
    return response;

}

    public CommonResponse  updateDriverStatus(DriverStatusUpdateRequest driverStatusUpdateRequest){
        CommonResponse<Object> response = new CommonResponse<>();

        DriverEntity driverEntity = driverRepository.findById(MDC.get(AopConstants.MDC_USERID)).orElseThrow(() -> {
            log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
            return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
        });

        try{
            String userId = driverEntity.getUserId();
            Optional<DriverStatusEntity> byDriversId = driverStatusRepository.findDriverStatusEntityByDriverId(userId);

            if (byDriversId.get().getStatus().equals(String.valueOf(DriverStatus.RUNNING))){
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            }else {
                byDriversId.get().setStatus(String.valueOf(driverStatusUpdateRequest.getStatus()));
                driverStatusRepository.save(byDriversId.get());
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.UPDATE_SUCCESS));
            }

        }catch (Exception ex) {
            log.error("Error occurred in update Driver Profile.Error:{}", ex.getMessage());
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }
        return response;
    }

    public CommonResponse  updateFcmToken(UpdateFcnTokenRequest updateFcnTokenRequest, HttpServletRequest request){
        CommonResponse<Object> response = new CommonResponse<>();

        UserTokenEntity userTokenEntity= userTokenRepository.findByUserId(MDC.get(AopConstants.MDC_USERID)).orElseThrow(() -> {
            log.error("User does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
            return new DomainException(Constants.ResponseData.USER_NOT_FOUND);
        });

        try {
            userTokenEntity.setFcmToken(updateFcnTokenRequest.getFcmToken());
            userTokenRepository.save(userTokenEntity);
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.UPDATE_SUCCESS));
        }catch (Exception ex) {
            log.error("Error occurred in updateCustomerProfile.Error:{}", ex.getMessage());
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }

        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.UPDATE_SUCCESS));
        return response;
    }
}
