package com.thinhbqt.enotes_api_service.service;

import com.thinhbqt.enotes_api_service.dto.UserDto;

public interface UserService {
    Boolean registerUser(UserDto userDto, String url);
}
