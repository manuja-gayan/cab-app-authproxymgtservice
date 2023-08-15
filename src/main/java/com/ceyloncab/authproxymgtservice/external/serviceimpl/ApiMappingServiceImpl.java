package com.ceyloncab.authproxymgtservice.external.serviceimpl;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.domain.boundary.ApiMappingService;
import com.ceyloncab.authproxymgtservice.domain.entity.UrlMappingEntity;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.HttpMethods;
import com.ceyloncab.authproxymgtservice.external.exception.ExternalException;
import com.ceyloncab.authproxymgtservice.external.repository.UrlMappingEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * ;
 */
@Slf4j
@Service
public class ApiMappingServiceImpl implements ApiMappingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UrlMappingEntityRepository urlMappingEntityRepository;

    /**
     * from this method handle all get api calls
     *
     * @param exposedUrl
     * @param queryParams
     * @param httpServletRequest
     * @return
     */
    @Override
    public ResponseEntity<String> externalGetAPICall(String exposedUrl, MultiValueMap<String, String> queryParams, HttpServletRequest httpServletRequest) {
        log.debug("externalGetAPICall method started for url {}", exposedUrl);
        ResponseEntity<String> responseEntity = null;

        try {
            UrlMappingEntity urlEntity = getUrlEntity(exposedUrl,HttpMethods.GET);
            // set headers
            HttpHeaders headers = getHeaders();
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            String url = urlBuilder(queryParams, urlEntity.getInternalUrl());
            log.info(String.format("External Api call|%s|%s", url, requestEntity));
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            log.info(String.format("External Api call|%s|%s|%s", url, responseEntity.getStatusCode(), responseEntity.getBody()));

        } catch (ResourceAccessException | HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error occurred in external GET request call.Error:{}", ex.getMessage(), ex);
            throw ex;
        }

        return responseEntity;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AopConstants.MDC_USERID, MDC.get(AopConstants.MDC_USERID));
        headers.set(AopConstants.CHANNEL, MDC.get(AopConstants.CHANNEL));
        headers.set(AopConstants.UUID, MDC.get(AopConstants.UUID));
        return headers;
    }

    /**
     * from this method handle all post api calls
     *
     * @param exposedUrl
     * @param requestBody
     * @param httpServletRequest
     * @return
     */
    @Override
    public ResponseEntity<String> externalPostAPICall(String exposedUrl, Map<String, Object> requestBody, HttpServletRequest httpServletRequest) {

        log.debug("externalPostAPICall method started for uri {}", exposedUrl);
        ResponseEntity<String> responseEntity = null;
        try {
            UrlMappingEntity urlEntity = getUrlEntity(exposedUrl,HttpMethods.POST);
            // set headers
            HttpHeaders headers = getHeaders();
            HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
            String url = urlEntity.getInternalUrl();
            log.info(String.format("External Api call|%s|%s", url, requestEntity));
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info(String.format("External Api call|%s|%s|%s", url, responseEntity.getStatusCode(), responseEntity.getBody()));

        } catch (ResourceAccessException | HttpClientErrorException | HttpServerErrorException ex) {
            throw ex;
        }
        return responseEntity;
    }

    private String urlBuilder(MultiValueMap<String, String> queryParams, String internalUrl) {
        // set url params
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(internalUrl);
        if (!queryParams.isEmpty()) {
            builder.queryParams(queryParams);
        }
        return builder.toUriString();
    }

    @Override
    @Cacheable(cacheNames = "urlCache")
    public UrlMappingEntity getUrlEntity(String exposedUrl,HttpMethods httpMethods) {
        log.info("Expose URL:{}|Method:{}",exposedUrl,httpMethods);
        Optional<UrlMappingEntity> urlEntityOptional = urlMappingEntityRepository.findOneByExposeUrlAndHttpMethod(exposedUrl, httpMethods);
        if(!urlEntityOptional.isPresent()){
            throw new ExternalException(Constants.ResponseData.URL_NOT_FOUND);
        }
        return urlEntityOptional.get();
    }
}
