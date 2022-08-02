package com.ElibraryDevelopment.Elibrary.Service.Impl;

import com.ElibraryDevelopment.Elibrary.Entity.PasswordResetTokenEntity;
import com.ElibraryDevelopment.Elibrary.Entity.UserEntity;
import com.ElibraryDevelopment.Elibrary.Exception.ClientSideException;
import com.ElibraryDevelopment.Elibrary.Model.Response.Messages;
import com.ElibraryDevelopment.Elibrary.Repository.PasswordResetTokenRepository;
import com.ElibraryDevelopment.Elibrary.Repository.UserRepository;
import com.ElibraryDevelopment.Elibrary.Security.SecurityConstants;
import com.ElibraryDevelopment.Elibrary.Service.EmailService;
import com.ElibraryDevelopment.Elibrary.Service.UserService;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.UserDto;
import com.ElibraryDevelopment.Elibrary.Shared.Dto.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public UserDto addUser(UserDto user) {

        UserEntity alreadyExsistingUser = userRepository.findByEmail(user.getEmail());
        if(alreadyExsistingUser !=null) throw new RuntimeException("Record already exists");
        UserEntity userEntity = new ModelMapper().map(user, UserEntity.class);
        userEntity.setUserId(utils.generateUserId(20));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String token = utils.generateEmailVerificationToken(userEntity.getUserId());
        userEntity.setEmailVerificationToken(token);
        userEntity.setEmailVerificationStatus(false);
        UserEntity storedUserDetails = userRepository.save(userEntity);

        String link = SecurityConstants.USER_CREATE_EMAIL_LINK + token;
        EmailBuilder emailBuilder = new EmailBuilder();
        emailService.send(userEntity.getEmail(), emailBuilder.
                buildRegistrationContent(userEntity.getFirstName(), link));
        return new ModelMapper().map(storedUserDetails,UserDto.class);
    }

    @Override
    public boolean verifyEmailToken(String token) throws ClientSideException {
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
        if (userEntity == null) throw new ClientSideException(Messages.TOKEN_NOT_FOUND.getMessage());
        if(Utils.hasTokenExpired(token)) throw new ClientSideException((Messages.EMAIL_TOKEN_EXPIRED.getMessage()));
        userEntity.setEmailVerificationToken(null);
        userEntity.setEmailVerificationStatus(Boolean.TRUE);
        try{
            userRepository.save(userEntity);
        }
        catch (Exception e){
            throw new ClientSideException(Messages.FAILED_DB_SAVE.getMessage());
        }
        return true;
    }

    @Override
    public Boolean requestEmailResend(String email) throws ClientSideException {;
        UserEntity alreadyExistingUser = userRepository.findByEmail(email);
        if (alreadyExistingUser == null)
            throw new ClientSideException(Messages.NO_RECORD_FOUND.getMessage());
        if(alreadyExistingUser.getEmailVerificationStatus())
            throw new ClientSideException(Messages.RECORD_ALREADY_EXISTS.getMessage());
        String token = utils.generateEmailVerificationToken(alreadyExistingUser.getUserId());
        alreadyExistingUser.setEmailVerificationToken(token);
        try{
            userRepository.save(alreadyExistingUser);
        }
        catch (Exception e){
            throw new ClientSideException(Messages.FAILED_DB_SAVE.getMessage());
        }
        String link = SecurityConstants.USER_CREATE_EMAIL_LINK + token;
        EmailBuilder emailBuilder = new EmailBuilder();
        emailService.send(alreadyExistingUser.getEmail(), emailBuilder.
                buildRegistrationContent(alreadyExistingUser.getFirstName(), link));
        return false;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);

//        UserDto returnValue = new UserDto();
//        BeanUtils.copyProperties(userEntity,returnValue);

        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public UserDto fetchUser(String userId) throws ClientSideException {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new ClientSideException(Messages.NO_RECORD_FOUND.getMessage());
        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) throws ClientSideException {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new ClientSideException(Messages.NO_RECORD_FOUND.getMessage());
        if (user.getFirstName() != null){
            userEntity.setFirstName(user.getFirstName());}
        if (user.getLastName() != null){
            userEntity.setLastName(user.getLastName());}
        if (user.getUserName() != null){
            userEntity.setUserName(user.getUserName());}
        UserEntity updatedUserDetails;
        try {
             updatedUserDetails = userRepository.save(userEntity);
        }
        catch (Exception e){
            throw new ClientSideException(Messages.FAILED_DB_SAVE.getMessage());
        }

        return new ModelMapper().map(updatedUserDetails,UserDto.class);

    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        Pageable pageableRequest = PageRequest.of(page,limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users){
            UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);
            returnValue.add(userDto);
        }
        return returnValue;
    }

    @Override
    public Boolean requestPasswordReset(String email) throws ClientSideException {

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new ClientSideException(Messages.EMAIL_NOT_FOUND.getMessage());

        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        try {
            passwordResetTokenRepository.save(passwordResetTokenEntity);
        } catch (Exception e) {
            throw new ClientSideException(Messages.FAILED_DB_SAVE.getMessage());
        }
        String link = SecurityConstants.PASSWORD_EMAIL_LINK + token;
        EmailBuilder emailBuilder = new EmailBuilder();
        emailService.send(userEntity.getEmail(), emailBuilder.buildPasswordResetContent(userEntity.getFirstName(), link));
        return true;
    }

    @Override
    public Boolean verifyPasswordResetToken(String token,String password1,String password2) throws ClientSideException {

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null)
            throw new ClientSideException(Messages.TOKEN_NOT_FOUND.getMessage());

        if (!password1.equals(password2))
            throw new ClientSideException(Messages.PASSWORD_NOT_MATCHING.getMessage());

        if(Utils.hasTokenExpired(token)) throw new ClientSideException((Messages.PASSWORD_TOKEN_EXPIRED.getMessage()));

        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(password1));
        try{
            userRepository.save(userEntity);
        }
        catch (Exception e){
            throw new ClientSideException(Messages.FAILED_DB_SAVE.getMessage());
        }
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        return true;
    }
    @Override
    public void deleteUser(String id) throws ClientSideException {
    UserEntity user =userRepository.findByUserId(id);
        System.out.println(user);
    if (user==null) throw new ClientSideException(Messages.NO_RECORD_FOUND.getMessage());
     userRepository.deleteUserById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity==null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
