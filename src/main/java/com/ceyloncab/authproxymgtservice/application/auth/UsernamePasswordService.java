package com.ceyloncab.authproxymgtservice.application.auth;

import com.ceyloncab.authproxymgtservice.domain.boundary.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService
                .getCustomerProfile(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("UserUUID %s not found", username))
                );
    }
}