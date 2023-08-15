package com.ceyloncab.authproxymgtservice.domain.boundary;

import com.ceyloncab.authproxymgtservice.domain.entity.UrlMappingEntity;
import com.ceyloncab.authproxymgtservice.domain.utils.HttpMethods;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * This is interface class for call mapped internal calls
 * from this interface class handle all external api calls  from common methods
 *
 */
public interface ApiMappingService {

    /**
     * to handle all GET api calls
     * @param uri
     * @param queryParams
     * @param httpServletRequest
     * @return
     */
    ResponseEntity<String> externalGetAPICall(String uri, MultiValueMap < String, String > queryParams, HttpServletRequest httpServletRequest);

    /**
     * to handle all POST api calls
     * @param uri
     * @param requestBody
     * @param httpServletRequest
     * @return
     */
    ResponseEntity<String> externalPostAPICall(String uri, Map < String, Object > requestBody, HttpServletRequest httpServletRequest);

    /**
     * get API url entity details for mapping purpose
     * @param exposedUrl
     * @param httpMethods
     * @return
     */
    UrlMappingEntity getUrlEntity(String exposedUrl, HttpMethods httpMethods);
}
