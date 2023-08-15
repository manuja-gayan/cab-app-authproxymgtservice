package com.ceyloncab.authproxymgtservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@Document(collection = "Complaint")
public class ComplaintEntity {
    @Id
    private String complaintId;
    private String firstName;
    private String lastName;
    private String subject;
    private String msisdn;
    private String email;
    private String description;

    @LastModifiedDate
    private Date updatedTime;

    @DocumentReference(lazy = true)
    private CustomerEntity customer;
}
