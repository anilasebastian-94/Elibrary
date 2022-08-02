package com.ElibraryDevelopment.Elibrary.Service;

import org.springframework.stereotype.Service;


public interface EmailService {

    boolean send(String to,String email);
}
