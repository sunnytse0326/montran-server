package com.montran.server.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Date;

public class MontranJWTService {
    private String subjectKey = "auth0";
    private String issuer = "montran";
    private String passwordSalt = "$2a$10$7URlO.wjhgFPHZpzXZi2I.";

    private JwtBuilder jwtBuilder;
    private long expirationTime = 60 * 60 * 24 * 1000;
    private Key key;
    private BCryptPasswordEncoder passwordEncoder;

    private static MontranJWTService instance;

    private MontranJWTService() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        passwordEncoder = new BCryptPasswordEncoder();

        jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(subjectKey);
        jwtBuilder.claim("issuer", issuer);
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(key);
        jwtBuilder.setExpiration(new Date(new Date().getTime() + expirationTime));
    }

    public static MontranJWTService getInstance() {
        if(instance == null){
            instance = new MontranJWTService();
        }
        return instance;
    }

    public String generatorJWT() {
        return jwtBuilder.compact();
    }

    public boolean verifyJWT(String token) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return jws.getBody().get("issuer").equals(issuer);
        } catch (JwtException ex) { }
        return false;
    }

    public String hashPassword(String password){
        return BCrypt.hashpw(password, passwordSalt);
    }

    public boolean verifyPassword(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
