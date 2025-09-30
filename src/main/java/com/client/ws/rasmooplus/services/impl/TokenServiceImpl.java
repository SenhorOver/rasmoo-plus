package com.client.ws.rasmooplus.services.impl;

import com.client.ws.rasmooplus.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${webservices.rasplus.jwt.expiration}")
    private String expiration;

    @Value("${webservices.rasplus.jwt.secret}")
    private String secret;



    @Override
    public String getToken(Long user_id) {
        Date today = new Date();
        Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));
        Key key = Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .issuer("API Rasmoo Plus")
                .subject(user_id.toString())
                .issuedAt(today)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    @Override
    public Boolean isValid(String token) {
        try {
            getClaimsJws(token, getSecretKey());
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public Long getUserId(String token) {
        Jws<Claims> claims = getClaimsJws(token, getSecretKey());
        return Long.parseLong(claims.getPayload().getSubject());
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }

    private static Jws<Claims> getClaimsJws(String token, SecretKey key) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }
}
