package com.ceyloncab.authproxymgtservice.external.repository;

import com.ceyloncab.authproxymgtservice.domain.entity.UserTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends MongoRepository<UserTokenEntity,String> {

    Optional<UserTokenEntity> findByUserUUIDAndUserRole(String userUUID,String userRole);
    Optional<UserTokenEntity> findByUserId(String userId);
}
