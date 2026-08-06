package com.thinhbqt.enotes_api_service.util;

public class Constants {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String MOBILE_REGEX = "^[789]\\d{9}$";

    public static final String ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String USER = "hasRole('ROLE_USER')";
    public static final String ADMIN_AND_USER = "hasAnyRole('ROLE_ADMIN','ROLE_USER')";

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
}
