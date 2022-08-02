package com.ElibraryDevelopment.Elibrary.Service;

import com.ElibraryDevelopment.Elibrary.Exception.ClientSideException;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto addUser(UserDto user);
    UserDto getUser(String email);
    UserDto fetchUser(String userId) throws ClientSideException;
    UserDto updateUser(String userId, UserDto user) throws ClientSideException;
    List<UserDto> getUsers(int page, int limit);
    void deleteUser(String id) throws ClientSideException;
    boolean verifyEmailToken(String token) throws ClientSideException;
    Boolean requestPasswordReset(String email) throws ClientSideException;
    Boolean verifyPasswordResetToken(String token, String newPassword, String repeatPassword) throws ClientSideException;

    Boolean requestEmailResend(String email) throws ClientSideException;
}
