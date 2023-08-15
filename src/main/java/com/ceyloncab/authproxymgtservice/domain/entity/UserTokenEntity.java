package com.ceyloncab.authproxymgtservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "UserToken")
@CompoundIndex(def = "{'userUUID':1, 'userRole':1}", name = "userUUID_role")
public class UserTokenEntity {
    @Id
    private String id;
    private String userId;
    private String userRole;
    private String userUUID;   //msisdn or email
    private String accessToken;
    private String refreshToken;
    private String sessionId;
    private String fcmToken="NOT_ASSIGNED";
    @LastModifiedDate
    private Date updatedTime;

}
