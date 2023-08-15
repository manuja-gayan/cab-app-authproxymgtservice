package com.ceyloncab.authproxymgtservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Admin")
public class AdminEntity {
    @Id
    private String userId;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String displayName;
    private List<String> actions;
    private Boolean isDelete = false;
    @LastModifiedDate
    private Date updatedTime;
}
