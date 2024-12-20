package com.project.CompanyService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Component
public class JwtTokenProvider {



    public List<String> extractRoles(String token, RSAPublicKey publicKey) {
        Claims claims = parseTokenClaims(token, publicKey);
        return claims.get("role", List.class);
    }

    public String extractUsername(String token, RSAPublicKey publicKey) {
        Claims claims = parseTokenClaims(token, publicKey);
        return claims.getSubject();
    }

    public boolean isTokenValidWithPublicKey(String token, RSAPublicKey publicKey) {
        try {
            parseTokenClaims(token, publicKey);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseTokenClaims(String token, RSAPublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}