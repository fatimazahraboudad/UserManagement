package com.project.UserService.security;

import com.project.UserService.entities.Role;
import com.project.UserService.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;


@Component
public class JwtTokenProvider {

    @Value("${token.private.key}")
    private String privateKeyPath;

    @Value("${token.public.key}")
    private String publicKeyPath;

    private PrivateKey privateKey;
    private PublicKey publicKey;
    @Value("${token.signing.key}")
    private String secretKey;

    @Value("${token.signing.expiration}")
    private long jwtExpiration;

    @Value("${token.signing.refresh-token.expiration}")
    private long refreshExpiration;


    @PostConstruct
    public void init() throws Exception {
        this.privateKey = loadPrivateKey(privateKeyPath);
        this.publicKey = loadPublicKey(publicKeyPath);
    }

    private String createToken(Map<String, Object> claims, String username, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }


    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = user.getRole().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        claims.put("role", roles);
        return createToken(claims, user.getEmail(), jwtExpiration);
    }
    public String generateRefreshToken(User user) {
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

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
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


    private PrivateKey loadPrivateKey(String path) throws Exception {
        // Charger la clé privée depuis le fichier
        String key = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

        // Supprimer les en-têtes et pieds de la clé (Begin/End)
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\n", "")  // Optionnel: supprimer les nouvelles lignes
                .trim();

        // Décoder la clé Base64
        byte[] decodedKey = Base64.getDecoder().decode(key);

        // Créer une clé privée à partir de la clé décodée
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    private PublicKey loadPublicKey(String path) throws Exception {
        // Charger la clé publique depuis le fichier
        String key = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

        // Supprimer les en-têtes et pieds de la clé (Begin/End)
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\n", "")  // Optionnel: supprimer les nouvelles lignes
                .trim();

        // Décoder la clé Base64
        byte[] decodedKey = Base64.getDecoder().decode(key);

        // Créer une clé publique à partir de la clé décodée (utiliser X509EncodedKeySpec pour la clé publique)
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
