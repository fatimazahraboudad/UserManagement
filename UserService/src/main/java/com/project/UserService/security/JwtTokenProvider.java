package com.project.UserService.security;

import com.project.UserService.entities.Role;
import com.project.UserService.entities.User;
import com.project.UserService.utils.LoadKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${token.private.key}")
    private String privateKeyPath;

    @Value("${token.public.key}")
    private String publicKeyPath;

    @Value("${token.signing.key}")
    private String secretKey;

    @Value("${token.signing.expiration}")
    private long jwtExpiration;

    @Value("${token.signing.refresh-token.expiration}")
    private long refreshExpiration;

    private final LoadKeys loadKeys;



    private String createToken(Map<String, Object> claims, String username, long expirationMillis) throws Exception {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(loadKeys.loadPrivateKey(privateKeyPath), SignatureAlgorithm.RS256)
                .compact();
    }


    public String generateAccessToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = user.getRole().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        claims.put("role", roles);
        return createToken(claims, user.getEmail(), jwtExpiration);
    }
    public String generateRefreshToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = user.getRole().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        claims.put("role", roles);
        return createToken(claims, user.getEmail(), refreshExpiration); // 1 hour
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) throws Exception {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws Exception {
        return Jwts
                .parserBuilder()
                .setSigningKey(loadKeys.loadPublicKey(publicKeyPath))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) throws Exception {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) throws Exception {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws Exception {
        return extractClaim(token, Claims::getExpiration);
    }


    public String generateEmailVerificationToken(String id){
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }




    public String generateAdminInvitationToken(String id){
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
