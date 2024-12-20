package com.project.CompanyService.security;

import com.project.CompanyService.utils.LoadKeys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoadKeys loadKeys;
    @Value("${token.public.key}")
    private String publicKeyPath;

    private RSAPublicKey publicKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            if (publicKey == null) {
                publicKey = loadKeys.loadPublicKey(publicKeyPath);
            }

            String userEmail = jwtTokenProvider.extractUsername(token, publicKey);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenProvider.isTokenValidWithPublicKey(token, publicKey)) {
                    log.info("Token valide pour l'utilisateur : {}", userEmail);


                    // Convertir les rôles en autorités
                    Collection<? extends GrantedAuthority> authorities = jwtTokenProvider.extractRoles(token, publicKey).stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());

                    // Créer l'objet d'authentification
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Définir l'authentification dans le contexte
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("Token expiré : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired.");
            return;
        } catch (JwtException e) {
            log.error("Token invalide : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
            return;
        } catch (Exception e) {
            log.error("Erreur dans le filtre JWT : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing the token.");
            return;
        }

        filterChain.doFilter(request, response);
    }


}
