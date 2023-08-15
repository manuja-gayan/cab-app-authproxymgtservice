package com.ceyloncab.authproxymgtservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Customer")
public class CustomerEntity {
	@Id
    private String userId;
	private String firstName;
	private String lastName;
	@Indexed(unique = true)
	private String msisdn;
	private String location;
	@LastModifiedDate
	private Date updatedTime;

	@ReadOnlyProperty
	@DocumentReference(lazy = true, lookup="{'customer':?#{#self._id} }")
	private List<ComplaintEntity> complaintEntityList;
}
