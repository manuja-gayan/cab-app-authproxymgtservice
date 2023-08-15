package com.ceyloncab.authproxymgtservice.application.auth;

import com.ceyloncab.authproxymgtservice.application.aop.AopConstants;
import com.ceyloncab.authproxymgtservice.application.auth.dto.AuthResponse;
import com.ceyloncab.authproxymgtservice.application.auth.dto.UserData;
import com.ceyloncab.authproxymgtservice.application.transport.request.auth.RefreshTokenRequest;
import com.ceyloncab.authproxymgtservice.domain.boundary.UserService;
import com.ceyloncab.authproxymgtservice.domain.entity.AdminEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.DriverEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.UserTokenEntity;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.login.CustomerRequest;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.authproxymgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.authproxymgtservice.domain.utils.Constants;
import com.ceyloncab.authproxymgtservice.domain.utils.UserRole;
import com.ceyloncab.authproxymgtservice.external.repository.AdminRepository;
import com.ceyloncab.authproxymgtservice.external.repository.DriverRepository;
import com.ceyloncab.authproxymgtservice.external.repository.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This is the service class for jwt token logic implementation.
 */
@Slf4j
@Service
public class JwtService {

    @Value("${security.jwt.sign.key}")
    private String jwtSignKey;

    @Value("${security.jwt.expiretime.access.token.millisecond}")
    private Integer accessTokenExpireTime;

    @Value("${security.jwt.expiretime.refresh.token.hour}")
    private Integer refreshTokenExpireTime;

    private final DriverRepository driverRepository;

    private final AdminRepository adminRepository;

    private final UserTokenRepository userTokenRepository;

    private final UserService userService;

    @Autowired
    public JwtService(DriverRepository driverRepository, AdminRepository adminRepository, UserTokenRepository userTokenRepository, UserService userService) {
        this.driverRepository = driverRepository;
        this.adminRepository = adminRepository;
        this.userTokenRepository = userTokenRepository;
        this.userService = userService;
    }


    // get all claims as map
    public Map<String, Object> getClaimsFromTokenAsMap(String token) {
        log.debug("Extracting claims");
        Claims claims = Jwts.parser().setSigningKey(jwtSignKey).parseClaimsJws(token).getBody();
        log.debug("Claims extracted");
        return new HashMap<>(claims);
    }

    //get user
    public UserData getUser(String userUUID, UserRole role) {
        switch (role) {
            case ADMIN:
                AdminEntity admin = adminRepository.findByEmail(userUUID)
                        .orElseThrow(() -> new UsernameNotFoundException("user not found for userUUID:".concat(userUUID)));
                return new UserData(admin.getUserId(), admin.getDisplayName(),
                        UserRole.ADMIN, admin.getEmail(), admin.getActions());
            case CUSTOMER:
                CustomerRequest customer = userService.getCustomerById(userUUID);
            case NONE:
                break;
            case DRIVER:
            default:
                DriverEntity driver = driverRepository.findByMsisdn(userUUID)
                        .orElseThrow(() -> new UsernameNotFoundException("user not found for userUUID:".concat(userUUID)));
                return new UserData(driver.getUserId(), driver.getFirstName() + " " + driver.getLastName(),
                        UserRole.DRIVER, driver.getMsisdn());
        }
        return new UserData();
    }

    private Optional<UserTokenEntity> getUserTokenEntity(String userUUID, String role) {
        return userTokenRepository.findByUserUUIDAndUserRole(userUUID, role);
    }

    //generate token for user
    public String generateToken(UserData userData, String tokenType, String sessionId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.USER_ROLE, userData.getUserRole());
        claims.put(JwtConstants.USER_ID, userData.getUserId());
        claims.put(JwtConstants.USERNAME, userData.getUsername());
        claims.put(JwtConstants.USER_UUID, userData.getUserUUID());
        claims.put(JwtConstants.SESSION_ID, sessionId);

        return doGenerateToken(claims, tokenType);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date expiration;

        long refreshExpiryTimeInMills = TimeUnit.HOURS.toMillis(refreshTokenExpireTime);

        if (TokenType.REFRESH_TOKEN.name().equals(subject)) {
            expiration = new Date(System.currentTimeMillis() + refreshExpiryTimeInMills);
        } else {
            expiration = new Date(System.currentTimeMillis() + accessTokenExpireTime);
        }

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSignKey).compact();
    }

    /**
     * validate token
     *
     * @param token
     * @param tokenType
     * @return
     */
    public Map<String, Object> validateToken(String token, TokenType tokenType, String headerUserId) {
        Map<String, Object> claimMap;
        String userRole;
        String userUUID;

        // extract all claims
        claimMap = getClaimsFromTokenAsMap(token);
        //adding authentication false as default
        claimMap.put(JwtConstants.IS_AUTHENTICATED,false);

        try {
            log.debug("Extracted claim map received");

            // extract user role from token
            userRole = (String) claimMap.get(JwtConstants.USER_ROLE);
            userUUID = (String) claimMap.get(JwtConstants.USER_UUID);
            String userId = (String) claimMap.get(JwtConstants.USER_ID);
            log.debug("user role {}", userRole);

            //validate header userId and token userId
            if (Boolean.FALSE.equals(userId.equals(headerUserId))) {
                return claimMap;
            }

            // get user details
            Optional<UserTokenEntity> userToken = getUserTokenEntity(userUUID, userRole);

            if (TokenType.ACCESS_TOKEN.equals(tokenType)
                    && userToken.isPresent() && userToken.get().getAccessToken().equals(token)) {
                log.debug("user token is found");
                log.debug("user token received {}", token);
                log.debug("user token in DB {}", userToken.get().getAccessToken());
                claimMap.put(JwtConstants.IS_AUTHENTICATED, true);
                return claimMap;
            } else if (TokenType.REFRESH_TOKEN.equals(tokenType)
                    && userToken.isPresent() && userToken.get().getRefreshToken().equals(token)) {
                log.debug("Token type is refresh token. Token found");
                claimMap.put(JwtConstants.IS_AUTHENTICATED, true);
                return claimMap;
            }

        } catch (Exception e) {
            return claimMap;
        }
        return claimMap;
    }

    public CommonResponse<AuthResponse> getRefreshToken(RefreshTokenRequest request){
        CommonResponse<AuthResponse> response = new CommonResponse<>();
        ResponseHeader header;
        try {
            Map<String, Object> validateResponse = validateToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN, MDC.get(AopConstants.MDC_USERID));
            if((boolean)validateResponse.get(JwtConstants.IS_AUTHENTICATED)){
                String sessionId = (String) validateResponse.get(JwtConstants.SESSION_ID);
                UserData user = getUser(String.valueOf(validateResponse.get(JwtConstants.USER_UUID)), Objects.nonNull(validateResponse.get(JwtConstants.USER_ROLE)) ?UserRole.valueOf(String.valueOf(validateResponse.get(JwtConstants.USER_ROLE))):UserRole.NONE);
                AuthResponse authResponse = new AuthResponse(null,null,null,
                        generateToken(user,TokenType.ACCESS_TOKEN.name(),sessionId),
                        request.getRefreshToken());
                header = new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS);
                response.setData(authResponse);
                saveUserToken(user,authResponse.getAccessToken(),authResponse.getRefreshToken(),sessionId);
            }else {
                header = new ResponseHeader(Constants.ResponseData.REFRESH_TOKEN_NOT_VALID);
            }

        }catch (Exception ex){
            log.error("Error occurred while generating refresh token.Error:{}", ex.getMessage(),ex);
            header = new ResponseHeader(Constants.ResponseData.REFRESH_TOKEN_NOT_VALID);
        }
        response.setResponseHeader(header);
        return response;
    }


    public void saveUserToken(UserData data, String accessToken, String refreshToken, String sessionId) {

        UserTokenEntity userToken;
        Optional<UserTokenEntity> existingToken = userTokenRepository.findByUserUUIDAndUserRole(data.getUserUUID(), data.getUserRole().name());
        userToken = existingToken.orElseGet(UserTokenEntity::new);
        userToken.setUserId(data.getUserId());
        userToken.setUserRole(data.getUserRole().name());
        userToken.setUserUUID(data.getUserUUID());
        userToken.setAccessToken(accessToken);
        userToken.setRefreshToken(refreshToken);
        userToken.setSessionId(sessionId);
        userTokenRepository.save(userToken);
    }
}
