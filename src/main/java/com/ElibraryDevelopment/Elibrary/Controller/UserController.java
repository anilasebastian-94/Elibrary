package com.ElibraryDevelopment.Elibrary.Controller;

import com.ElibraryDevelopment.Elibrary.Entity.UserEntity;
import com.ElibraryDevelopment.Elibrary.Exception.ClientSideException;
import com.ElibraryDevelopment.Elibrary.Exception.Validation;
import com.ElibraryDevelopment.Elibrary.Model.Request.EmailRequestModel;
import com.ElibraryDevelopment.Elibrary.Model.Request.PasswordResetModel;
import com.ElibraryDevelopment.Elibrary.Model.Request.UserDetailsRequestModel;
import com.ElibraryDevelopment.Elibrary.Model.Response.Messages;
import com.ElibraryDevelopment.Elibrary.Model.Response.UserRest;
import com.ElibraryDevelopment.Elibrary.Repository.UserRepository;
import com.ElibraryDevelopment.Elibrary.Security.SecurityConstants;
import com.ElibraryDevelopment.Elibrary.Service.EmailService;
import com.ElibraryDevelopment.Elibrary.Service.Impl.EmailBuilder;
import com.ElibraryDevelopment.Elibrary.Service.UserService;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.UserDto;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.Utils;
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

    @Autowired
    EmailService emailService;

    @Autowired
    Utils utils;

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

    @GetMapping(path = "/email/verify")
    public ResponseEntity<String> verifyEmailToken(@RequestParam(value = "token") String token) throws ClientSideException{
        boolean isVerified = userService.verifyEmailToken(token);
        if (!isVerified) throw new
                ClientSideException(Messages.EMAIL_ADDRESS_NOT_VERIFIED.getMessage());
        return new ResponseEntity<String>(Messages.EMAIL_ADDRESS_VERIFIED.getMessage(), HttpStatus.OK);
    }

    @PostMapping(path ="/resend/verification")
    public ResponseEntity<String> requestEmailResend(@RequestBody EmailRequestModel emailRequestModel) throws ClientSideException {

        Boolean OperationResult = userService.requestEmailResend(emailRequestModel.getEmail());

        if(OperationResult)
            return new ResponseEntity<String>(Messages.EMAIL_SENT.getMessage(),HttpStatus.OK);
        return new ResponseEntity<String>(Messages.EMAIL_NOT_SENT.getMessage(),HttpStatus.EXPECTATION_FAILED);
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

    @PostMapping(path="/password/reset")
    public ResponseEntity<String> requestReset(@RequestBody EmailRequestModel emailRequestModel) throws ClientSideException {

        Boolean OperationResult = userService.requestPasswordReset(emailRequestModel.getEmail());

        if(OperationResult)
            return new ResponseEntity<String>(Messages.EMAIL_SENT.getMessage(),HttpStatus.OK);
        return new ResponseEntity<String>(Messages.EMAIL_NOT_SENT.getMessage(),HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping(path="/password/create")
    public ResponseEntity<String> setNewPassword(@RequestBody PasswordResetModel passwordResetModel,
                                                 @RequestParam(value = "token") String token) throws ClientSideException {
        Boolean isVerified = userService.verifyPasswordResetToken(token,passwordResetModel.getNewPassword(),
                passwordResetModel.getRepeatPassword());
        if(isVerified) return new ResponseEntity<String>(Messages.PASSWORD_CHANGED.getMessage(),HttpStatus.OK);
        return new ResponseEntity<String>(Messages.PASSWORD_NOT_CHANGED.getMessage(),HttpStatus.EXPECTATION_FAILED);
    }

    @DeleteMapping(path = "/deleteUser/{id}")
    public ResponseEntity<String>deleteUser(@RequestParam("id") String id) throws ClientSideException {
        userService.deleteUser(id);
        return new ResponseEntity<String>(Messages.DELETE_SUCCESS.getMessage(),HttpStatus.OK);
    }
}

