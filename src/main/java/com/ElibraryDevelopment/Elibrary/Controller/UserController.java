package com.ElibraryDevelopment.Elibrary.Controller;

import com.ElibraryDevelopment.Elibrary.Model.Request.UserDetailsRequestModel;
import com.ElibraryDevelopment.Elibrary.Model.Response.UserRest;
import com.ElibraryDevelopment.Elibrary.Service.UserService;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "elibrary")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/user/{userId}")
    public UserRest fetchUser(@PathVariable String userId ) {
        UserDto userDto = userService.fetchUser(userId);
        return new ModelMapper().map(userDto,UserRest.class);
    }

    @PostMapping(path = "/adduser")
    public UserRest addUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserDto userDto = new ModelMapper().map(userDetails,UserDto.class);
        UserDto createUser = userService.addUser(userDto);
        return new ModelMapper().map(createUser,UserRest.class);
    }
}

