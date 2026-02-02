package com.yalchenko.task_manager.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String generateToken(Authentication authentication){
        var userPrincipal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return Jwts.builder()
                //Получаем email пользователя
                .setSubject(userPrincipal.getUsername())
                //Берем роль пользователя и возвращаем её название строкой
                .claim("role", userPrincipal.getAuthorities().iterator().next().getAuthority())
                //Дата создания токена
                .setIssuedAt(new Date())
                //Срок действия токена
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                //Хешируем + jwtSecret
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }

}
