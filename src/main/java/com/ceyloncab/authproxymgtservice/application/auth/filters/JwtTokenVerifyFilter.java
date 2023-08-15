package com.ceyloncab.authproxymgtservice.application.auth.filters;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.auth.JwtConstants;
import com.ceyloncab.authproxymgtservice.application.auth.JwtService;
import com.ceyloncab.authproxymgtservice.application.auth.TokenType;
import com.ceyloncab.authproxymgtservice.domain.boundary.ApiMappingService;
import com.ceyloncab.authproxymgtservice.domain.entity.UrlMappingEntity;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.HttpMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JwtTokenVerifyFilter extends OncePerRequestFilter {

    private final List<String> nonAuthPattern;

    private final JwtService jwtService;

    private final ApiMappingService apiMappingService;

    public JwtTokenVerifyFilter(JwtService jwtService, ApiMappingService apiMappingService, String[] nonAuthPattern) {
        this.jwtService = jwtService;
        this.nonAuthPattern = Arrays.asList(nonAuthPattern);
        this.apiMappingService = apiMappingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(JwtConstants.AUTH_HEADER_TEXT);

        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(JwtConstants.BEARER)) {
            sendErrorResponse(Constants.ResponseData.TOKEN_NOT_VALID.getMessage(),request,response);
            return;
        }

        String token = authorizationHeader.replace(JwtConstants.BEARER, "");

        try {
            Map<String,Object> claimMap = jwtService.validateToken(token, TokenType.ACCESS_TOKEN,request.getHeader(AopConstants.MDC_USERID));
            if ((Boolean) claimMap.get(JwtConstants.IS_AUTHENTICATED)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UrlMappingEntity mappingEntity = apiMappingService.getUrlEntity(request.getRequestURI(), HttpMethods.valueOf(request.getMethod()));
                if(!mappingEntity.getAccessibleRoles().contains((String) claimMap.get(JwtConstants.USER_ROLE))){
                    sendErrorResponse(Constants.ResponseData.ACCESS_DENIED.getMessage(), request,response);
                    return;
                }
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority((String) claimMap.get(JwtConstants.USER_ROLE)));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claimMap.get(JwtConstants.USER_UUID),
                        null,
                        authorities);            // Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("Authentication is set");
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            sendErrorResponse(ex.getMessage(),request,response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(String msg, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,String> error = new HashMap<>();
        error.put("code", Constants.UNHANDLED_ERROR_CODE);
        error.put("message", msg);
        response.setContentType( MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader(AopConstants.CHANNEL, request.getHeader(AopConstants.CHANNEL));
        response.addHeader(AopConstants.MDC_USERID, request.getHeader(AopConstants.MDC_USERID));
        response.addHeader(AopConstants.UUID, request.getHeader(AopConstants.UUID));
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return nonAuthPattern.contains(request.getServletPath());
    }


}
