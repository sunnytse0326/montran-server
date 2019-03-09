package com.montran.server.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Key;
import java.util.Date;

public class MontranJWTService {
    private String subjectKey = "auth0";
    private String issuer = "montran";
    private String emailTag = "email";
    private String issuerTag = "issuerTag";
    private String passwordSalt = "$2a$10$7URlO.wjhgFPHZpzXZi2I.";

    private String transactionURL = "https://sunnytse0326.github.io/MockJson/transaction/result.json";

    private WebClient.Builder webClientBuilder;

    private JwtBuilder jwtBuilder;
    private long expirationTime = 60 * 60 * 24 * 1000;
    private Key key;
    private BCryptPasswordEncoder passwordEncoder;

    private static MontranJWTService instance;

    private MontranJWTService() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        passwordEncoder = new BCryptPasswordEncoder();
        webClientBuilder = WebClient.builder();

        jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(subjectKey);
        jwtBuilder.claim(issuerTag, issuer);
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(key);
        jwtBuilder.setExpiration(new Date(new Date().getTime() + expirationTime));
    }

    public static MontranJWTService getInstance() {
        if (instance == null) {
            instance = new MontranJWTService();
        }
        return instance;
    }

    public String generatorJWT(String email) {
        return jwtBuilder.claim(emailTag, email).compact();
    }

    public boolean verifyJWT(String token) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return jws.getBody().get(issuerTag).equals(issuer);
        } catch (JwtException ex) {
        }
        return false;
    }

    public Claims getJWTClaimerInfo(String token) {
        try {
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return jws.getBody();
        } catch (JwtException ex) {
        }
        return null;
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, passwordSalt);
    }

    public boolean verifyPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public String getEmailTag(){
        return emailTag;
    }

    public String getTransactionURL(){
        return transactionURL;
    }

    public WebClient.Builder getWebClientBuilder(){
        return webClientBuilder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
