package com.ceyloncab.authproxymgtservice.external.repository;

import com.ceyloncab.authproxymgtservice.domain.entity.DriverEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverStatusEntity;

import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverStatusRepository extends MongoRepository<DriverStatusEntity,String> {
//    GeoResults<DriverStatusEntity> findByLocationNearAndVehicleType(Point point, Distance distance, VehicleType vehicleType, Pageable pageable);
//    Page<DriverStatusEntity> findByLocationNearAndVehicleTypeAndStatusIDLE(Point point, Distance distance, String vehicleType, Pageable pageable);
      List<DriverStatusEntity> findByVehicleTypeAndStatusAndLocationNear(VehicleType vehicleType,String status, GeoJsonPoint point, Distance distance);
    /* List<DriverStatusEntity> findByLocationAndVehicleType(GeoJsonPoint location, VehicleType vehicleType);*/
     List<DriverStatusEntity> findByDriverId(String driverId );
    Optional<DriverStatusEntity> findDriverStatusEntityByDriverId(String driverId);





}

