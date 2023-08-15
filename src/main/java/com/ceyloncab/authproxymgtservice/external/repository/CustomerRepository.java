package com.ceyloncab.authproxymgtservice.external.repository;

import com.ceyloncab.authproxymgtservice.domain.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerEntity,String> {
}
