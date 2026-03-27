package com.zinn.zinnbe.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.zinn.zinnbe.models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtils {
    private final String secretVal = "my_secret_key_long_enough_usable";
    private final SecretKey secret = Keys.hmacShaKeyFor(secretVal.getBytes(StandardCharsets.UTF_8));
    public String buildToken(Users user){
        HashMap<String, String> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("id", user.getId().toString());
        return Jwts.builder().signWith(secret).subject(user.getEmail()).claim("user", claims).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).compact();
    }

    public String extractEmail(String token){
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Claims getClaims(String token){
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
    }

    public Boolean validateToken(String token, Users user){
        Claims claims = getClaims(token);
        if(claims.getSubject().matches(user.getEmail())){
            return !claims.getExpiration().before(new Date());
        }
        return false;
    }
}
