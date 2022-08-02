package com.ElibraryDevelopment.Elibrary.Shared.Dto;

import com.ElibraryDevelopment.Elibrary.Security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public class Utils {

    private final SecureRandom RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public String generateUserId(int length) {
        return generateRandomString(length);
    }
    public String generateRandomString(int lenght) {
        StringBuilder returnValue = new StringBuilder(lenght);

        for (int i = 0; i < lenght; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public String generateEmailVerificationToken(String userId){
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.EMAIL_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.getTokenSecret())
                .compact();
        return token;
    }

    public static boolean hasTokenExpired(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token).getBody();
            Date tokenExpirationTime = claims.getExpiration();
            Date currentTime = new Date();
            System.out.println(tokenExpirationTime.before(currentTime));
            return tokenExpirationTime.before(currentTime);
        }
        catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public String generatePasswordResetToken(String userId){
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EMAIL_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.getTokenSecret())
                .compact();
        return token;
    }
}
