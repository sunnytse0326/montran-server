package com.montran.server.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class MontranJWTService {
    private String subjectKey = "auth0";
    private String issuer = "montran";
    private JwtBuilder jwtBuilder;
    private long expirationTime = 60 * 60 * 24 * 1000;
    private Key key;

    private static final MontranJWTService instance = new MontranJWTService();

    private MontranJWTService() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(subjectKey);
        jwtBuilder.claim("issuer", issuer);
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(key);
        jwtBuilder.setExpiration(new Date(new Date().getTime() + expirationTime));
    }

    public static MontranJWTService getInstance() {
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
}
