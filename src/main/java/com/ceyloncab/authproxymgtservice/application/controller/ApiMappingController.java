package com.ceyloncab.authproxymgtservice.application.controller;

import com.ceyloncab.authproxymgtservice.domain.boundary.ApiMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *  use to map exposed api urls with internal api urls
 */
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("${base-url.context}/api")
public class ApiMappingController extends BaseController{

    @Autowired
    private ApiMappingService apiMappingService;

    /**
     * Intercepts all the GET requests that starts with /api
     *
     * @param httpServletRequest
     * @param queryParams
     * @return
     */
    @GetMapping(value = "/**")
    public ResponseEntity< ? > exposedGetRequestForward(@RequestParam(required = false) MultiValueMap< String, String > queryParams,
                                                        HttpServletRequest httpServletRequest) {


        String uri = httpServletRequest.getRequestURI();
        ResponseEntity< String > responseEntity = apiMappingService.externalGetAPICall(uri, queryParams, httpServletRequest);
        log.debug("External GET response : {}", responseEntity);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(responseEntity.getBody());
    }

    /**
     * Intercepts all the POST requests that starts with /api
     *
     * @param requestBody
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< ? > exposedPostRequestForward(@RequestBody(required = false) Map< String, Object > requestBody,
                                                          HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        ResponseEntity< String > responseEntity = apiMappingService.externalPostAPICall(uri, requestBody, httpServletRequest);
        log.debug("External POST response : {}", responseEntity);
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(responseEntity.getBody());
    }
}
