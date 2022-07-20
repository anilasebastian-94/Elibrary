package com.ElibraryDevelopment.Elibrary.Controller;

import com.ElibraryDevelopment.Elibrary.Entity.UserEntity;
import com.ElibraryDevelopment.Elibrary.Exception.ClientSideException;
import com.ElibraryDevelopment.Elibrary.Exception.Validation;
import com.ElibraryDevelopment.Elibrary.Model.Request.UserDetailsRequestModel;
import com.ElibraryDevelopment.Elibrary.Model.Response.Messages;
import com.ElibraryDevelopment.Elibrary.Model.Response.UserRest;
import com.ElibraryDevelopment.Elibrary.Repository.UserRepository;
import com.ElibraryDevelopment.Elibrary.Service.UserService;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "elibrary")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/user/{userId}")
    public UserRest fetchUser(@PathVariable String userId ) throws ClientSideException {
        UserDto userDto = userService.fetchUser(userId);
        return new ModelMapper().map(userDto,UserRest.class);
    }

    @PostMapping(path = "/adduser")
    public ResponseEntity<UserRest> addUser(@RequestBody UserDetailsRequestModel userDetails) throws ClientSideException {

        Validation validation = new Validation();
        if (!validation.checkFields(userDetails)) throw new ClientSideException(Messages.MISSING_REQUIRED_FIELD.getMessage());
        UserDto userDto = new ModelMapper().map(userDetails,UserDto.class);
        UserDto createUser = userService.addUser(userDto);
        return new ResponseEntity<>(new ModelMapper().map(createUser,UserRest.class), HttpStatus.CREATED);
    }

    @PutMapping(path = "/updateUser/{userId}")
    public ResponseEntity<UserRest> updateUser(@PathVariable String userId,@RequestBody UserDetailsRequestModel userDetails) throws ClientSideException{

        UserDto userDto = new ModelMapper().map(userDetails,UserDto.class);
        UserDto updatedUser = userService.updateUser(userId,userDto);
        return new ResponseEntity<>(new ModelMapper().map(updatedUser,UserRest.class), HttpStatus.OK);
    }

    @GetMapping(path = "/getUsers")
    public List<UserRest> getUsers(@RequestParam(value = "page",defaultValue = "0") int page,
                                   @RequestParam(value = "limit",defaultValue = "2") int limit) {
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page,limit);

        for (UserDto userDto : users){
            UserRest userRest = new ModelMapper().map(userDto,UserRest.class);
            returnValue.add(userRest);
        }
        return returnValue;
    }
}

