package com.ceyloncab.authproxymgtservice.external.repository;

import com.ceyloncab.authproxymgtservice.domain.entity.UrlMappingEntity;
import com.ceyloncab.authproxymgtservice.domain.utils.HttpMethods;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingEntityRepository extends MongoRepository<UrlMappingEntity,String> {

    Optional<UrlMappingEntity> findOneByExposeUrlAndHttpMethod(String exposeUrl, HttpMethods httpMethods);
}
