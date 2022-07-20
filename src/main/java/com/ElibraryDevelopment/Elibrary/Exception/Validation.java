package com.ElibraryDevelopment.Elibrary.Exception;

import com.ElibraryDevelopment.Elibrary.Model.Request.UserDetailsRequestModel;

public class Validation {

    public Boolean checkFields(UserDetailsRequestModel userDetails){

        if(userDetails.getFirstName()==null || userDetails.getFirstName().isEmpty()) return false;
        if(userDetails.getLastName()==null || userDetails.getLastName().isEmpty()) return false;
        if(userDetails.getEmail()==null ||userDetails.getEmail().isEmpty()) return false;
        if(userDetails.getPassword()==null ||userDetails.getPassword().isEmpty()) return false;
        if(userDetails.getUserName()==null ||userDetails.getUserName().isEmpty()) return false;

        return true;
    }
}
