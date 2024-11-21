package com.project.SubscriptionService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Component
public class JwtTokenProvider {

    private RSAPublicKey publicKey;

    public RSAPublicKey loadPublicKey(String filePath) throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "")
                .trim();
        byte[] decodedKey = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    // Extraire les rôles du token
    public List<String> extractRoles(String token, RSAPublicKey publicKey) {
        Claims claims = parseTokenClaims(token, publicKey);
        // Assurez-vous que le claim "roles" contient une liste de chaînes
        return claims.get("role", List.class);
    }

    public String extractUsername(String token, RSAPublicKey publicKey) {
        Claims claims = parseTokenClaims(token, publicKey);
        return claims.getSubject();
    }

    public boolean isTokenValidWithPublicKey(String token, RSAPublicKey publicKey) {
        try {
            parseTokenClaims(token, publicKey);
            return true; // Si aucune exception n'est levée, le token est valide
        } catch (JwtException e) {
            return false; // Une exception indique que le token est invalide
        }
    }

    // Méthode privée pour analyser les claims du token
    private Claims parseTokenClaims(String token, RSAPublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey) // Utiliser la clé publique pour vérifier la signature
                .build()
                .parseClaimsJws(token)
                .getBody(); // Retourne les claims si la signature est valide
    }
}