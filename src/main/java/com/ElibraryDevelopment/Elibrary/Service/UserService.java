package com.ElibraryDevelopment.Elibrary.Service;

import com.ElibraryDevelopment.Elibrary.Shared.Dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto addUser(UserDto user);
    UserDto getUser(String email);
    UserDto fetchUser(String userId);
}
