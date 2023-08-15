package com.ceyloncab.authproxymgtservice.domain.entity.dto.login;

import com.ceyloncab.authproxymgtservice.domain.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String userId;
    @NotNull( message = "msisdn not found for operation. This action is not allowed" )
    @Valid
    private String msisdn;
    @NotNull( message = "firstName not found for operation. This action is not allowed" )
    @Valid
    private String firstName;
    @NotNull( message = "lastName not found for operation. This action is not allowed" )
    @Valid
    private String lastName;
    private String location;
    private String  imgProfile;
    @NotNull( message = "location not found for operation. This action is not allowed" )
    @Valid
    private String email;
    private Status status =Status.ACTIVE;
    private Boolean isFirstLogin;
}
