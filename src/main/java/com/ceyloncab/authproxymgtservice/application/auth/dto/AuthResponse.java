package com.ceyloncab.authproxymgtservice.application.auth.dto;

import com.ceyloncab.authproxymgtservice.domain.utils.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse{

    private String userId;
    private UserRole userRole;
    private String userUUID;
    private String accessToken;
    private String refreshToken;

    private List<String> actions;

    private Boolean isFirstLogin;
    public AuthResponse(String userId, UserRole userRole, String userUUID, String accessToken, String refreshToken) {
        this.userId = userId;
        this.userRole = userRole;
        this.userUUID = userUUID;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper= new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
