package com.ceyloncab.authproxymgtservice.application.exception;

import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Getter
@Setter
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * Handle access denied exception
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException {

        CommonResponse response = new CommonResponse();
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.ACCESS_DENIED));
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
