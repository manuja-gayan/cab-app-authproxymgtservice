package com.ceyloncab.authproxymgtservice.domain.entity;

import com.ceyloncab.authproxymgtservice.domain.utils.DriverStatus;
import com.ceyloncab.authproxymgtservice.domain.utils.VehicleType;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document(collection = "DriverStatus")
public class DriverStatusEntity {
    @Id
    private String id;
    private String status =String.valueOf(DriverStatus.OFFLINE);
    @Indexed(unique = true)
    private String driverId;
    private VehicleType vehicleType;
    @GeoSpatialIndexed(name = "location", type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location=null;
    private Double avgSpeed=0.0;
    @LastModifiedDate
    private Date updatedTime;

    @DocumentReference(lazy = true)
    private DriverEntity driver;
}
