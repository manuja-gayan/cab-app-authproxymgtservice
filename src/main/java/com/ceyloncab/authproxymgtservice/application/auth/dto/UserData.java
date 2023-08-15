package com.ceyloncab.authproxymgtservice.application.auth.dto;

import com.ceyloncab.authproxymgtservice.domain.utils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String userId;
    private String username;
    private UserRole userRole;
    private String userUUID;

    private List<String> actions;

    public UserData(String userId, String username, UserRole userRole, String userUUID) {
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.userUUID = userUUID;
    }

}
