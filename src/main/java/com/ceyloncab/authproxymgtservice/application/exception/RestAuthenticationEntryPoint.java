package com.ceyloncab.authproxymgtservice.application.exception;

import com.ceyloncab.authproxymgtservice.application.auth.JwtConstants;
import com.ceyloncab.authproxymgtservice.application.auth.JwtService;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * This is the custom authentication entry point class
 *
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private JwtService jwtService;

    public RestAuthenticationEntryPoint(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Handle all authentication exceptions
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        CommonResponse response = new CommonResponse();
        ResponseHeader responseHeader;
        if (e instanceof InsufficientAuthenticationException) {
            responseHeader = new ResponseHeader();
            responseHeader.setMessage(e.getMessage());
            responseHeader.setCode(Constants.UNHANDLED_ERROR_CODE);
        }else {
            responseHeader = new ResponseHeader(Constants.ResponseData.ACCESS_DENIED);
        }

        response.setResponseHeader(responseHeader);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();

    }
}
