package com.ceyloncab.authproxymgtservice.application.auth.filters;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.auth.JwtService;
import com.ceyloncab.authproxymgtservice.application.auth.TokenType;
import com.ceyloncab.authproxymgtservice.application.auth.dto.AuthResponse;
import com.ceyloncab.authproxymgtservice.application.auth.dto.DriverLoginRequest;
import com.ceyloncab.authproxymgtservice.application.auth.dto.UserData;
import com.ceyloncab.authproxymgtservice.domain.utils.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            DriverLoginRequest driverLogin = new ObjectMapper()
                    .readValue(request.getInputStream(), DriverLoginRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    driverLogin.getMsisdn(),
                    driverLogin.getPassword()
            );

            MDC.put(AopConstants.CHANNEL,request.getHeader(AopConstants.CHANNEL));
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserData user = jwtService.getUser(authResult.getName(), Objects.nonNull(authResult.getAuthorities()) &&
                !authResult.getAuthorities().isEmpty()?UserRole.valueOf(authResult.getAuthorities().toArray()[0].toString()):UserRole.NONE);
        String sessionId = request.getSession().getId();


        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwtService.generateToken(user, TokenType.ACCESS_TOKEN.name(),sessionId));
        authResponse.setRefreshToken(jwtService.generateToken(user,TokenType.REFRESH_TOKEN.name(),sessionId));
        authResponse.setUserRole(user.getUserRole());
        authResponse.setUserUUID(user.getUserUUID());
        authResponse.setUserId(user.getUserId());
        authResponse.setActions(user.getActions());
        jwtService.saveUserToken(user,authResponse.getAccessToken(),authResponse.getRefreshToken(),sessionId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(authResponse.toString());
        response.addHeader(AopConstants.CHANNEL, user.getUserRole().name());
        response.addHeader(AopConstants.MDC_USERID, user.getUserId());
        response.addHeader(AopConstants.UUID, request.getHeader(AopConstants.UUID));
    }
}