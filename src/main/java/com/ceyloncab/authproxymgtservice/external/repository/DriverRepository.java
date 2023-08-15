package com.ceyloncab.authproxymgtservice.external.repository;

import com.ceyloncab.authproxymgtservice.domain.entity.DriverEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends MongoRepository<DriverEntity,String> {

    Optional<DriverEntity> findByMsisdn(String msisdn);
}
