package com.ElibraryDevelopment.Elibrary.Service.Impl;

public class EmailBuilder {
    public String buildRegistrationContent(String name,String link) {
        return "Dear "+name+",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href="+link+" target=\"_self\">VERIFY</a></h3>"
                + "Thank you.<br>";
    }

    public String buildPasswordResetContent(String name,String link) {
        return "Dear "+name+",<br>"
                + "Please click the link below to change password:<br>"
                + "<h3><a href="+link+" target=\"_self\">VERIFY</a></h3>"
                + "Thank you.<br>";
    }
}
