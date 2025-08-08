package com.example.coffee.management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
   @Value("${jwt.secret}")
   private String secretKey;
   private static final long EXPIRATION_TIME_ACCESS = 15 * 60 * 1000;
   private static final long EXPIRATION_TIME_REFRESH = 7 * 24 * 60 * 60 * 1000;
   private Key key;

   @PostConstruct
   public void init() {
      this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
   }

   // Tao Jwt ACCESS Token
   public String generateToken(String email, String role) {
      return Jwts.builder()
             .setSubject(email)
             .claim("role", role) // Lưu role vào payload
             .setIssuedAt(new Date())
             .setExpiration((new Date(System.currentTimeMillis() + EXPIRATION_TIME_ACCESS)))
             .signWith(key, SignatureAlgorithm.HS256)
             .compact();

   }

   //Tao REFRESH Token
   public String generateRefreshToken(String email) {
      return Jwts.builder()
             .setSubject(email)
             .setIssuedAt(new Date())
             .setExpiration((new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH)))
             .signWith(key, SignatureAlgorithm.HS256)
             .compact();
   }

   // Giai ma Jwt
   public Claims extractToken(String token) {
      return Jwts.parserBuilder()
             .setSigningKey(key)
             .build()
             .parseClaimsJws(token)
             .getBody();

   }

   public String getEmail(String token) {
      return extractToken(token).getSubject();
   }

   // Giai ma token lay role
   public String extractRole(String token) {
      return (String) extractToken(token).get("role");
   }

   public boolean validateToken(String token, UserDetails userDetails) {
      try {
         Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
         return true;
      } catch (Exception e) {
         return false;
      }
   }

}
