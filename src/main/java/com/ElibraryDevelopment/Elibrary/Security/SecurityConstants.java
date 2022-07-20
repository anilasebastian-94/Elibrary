package com.ElibraryDevelopment.Elibrary.Security;

import com.ElibraryDevelopment.Elibrary.SpringApplicationContext;

public class SecurityConstants {
    public static final String SIGN_UP_URL = "/elibrary/adduser";
    public static final long EXPIRATION_TIME = 864000000;
    public static final long EMAIL_EXPIRATION_TIME = 600000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();

    }

}
