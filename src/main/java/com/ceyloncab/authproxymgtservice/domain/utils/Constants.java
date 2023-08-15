package com.ceyloncab.authproxymgtservice.domain.utils;

import lombok.Getter;

public class

Constants {
    public static final String UNHANDLED_ERROR_CODE = "APMS3000";
    public static final String TOPIC_TRIP = "trip";
    @Getter
    public enum ResponseData {
        CREATE_SUCCESS("APMS1000", "Success","201"),
        QUERY_SUCCESS("APMS1001", "Query Success","200"),
        VERIFY_SUCCESS("APMS1002", "Verified","200"),
        UPDATE_SUCCESS("UMS1003", "Update Success","200"),
        PASSWORD_NOT_MATCHED("APMS2001", "Password not matched","400"),
        COMMON_FAIL("APMS2000", "Failed","400"),
        DRIVER_NOT_FOUND("APMS2002", "Driver not found","400"),
        USER_NOT_FOUND("APMS2002", "User not found","400"),
        CUSTOMER_NOT_FOUND("APMS2003", "Customer not found","400"),
        ACCESS_DENIED("APMS2004", "Access denied","401"),
        TOKEN_NOT_VALID("APMS2005", "Not a valid token","401"),
        REFRESH_TOKEN_NOT_VALID("APMS2006", "Refresh token not valid","401"),
        COMMON_SUCCESS("APMS1003", "Success","200"),
        URL_NOT_FOUND("APMS2007", "Url Not Found","404"),
        CUSTOMER_FETCH_FAILED("APMS2008", "Failed to get customer profile","500"),
        INTERNAL_SERVER_ERROR("APMS3001", "Internal Server Error","500");

        private final String code;
        private final String message;
        private final String responseCode;

        ResponseData(String code, String message, String responseCode) {
            this.code = code;
            this.message = message;
            this.responseCode= responseCode;
        }
    }
}
