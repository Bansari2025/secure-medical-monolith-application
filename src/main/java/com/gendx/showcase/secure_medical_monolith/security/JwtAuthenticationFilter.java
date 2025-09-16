package com.gendx.showcase.secure_medical_monolith.security;

/*This class inspects every incoming HTTP request for a valid JWT
* in the Authorization header*/

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * A custom security filter that intercepts every incoming HTTP request.
 * Its purpose is to inspect for a JWT in the 'Authorization' header and, if valid,
 * to set the user's authentication context for the duration of the request.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    //This method is executed for every request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // 1. Extract the Authorization header.
        String authHeader = request.getHeader("Authorization");

        // 2. If the header is missing or doesn't start with "Bearer ",
        //    it's not a JWT request, so we pass it along the chain without processing.
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the token string itself
        String token = authHeader.substring(7);

        try {
            //4. Validate the token using our JwtService.
            // This will throw an exception if the token is invalid (e.g., expired, wrong signature).
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            String username = decodedJWT.getSubject();
            String role = decodedJWT.getClaim("role").asString();

            // 5. If the token is valid, create an authentication object.
            // This is the standard Spring Security object for representing an authenticated user.

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority(role))
            );
            //6. Set the authentication object in the security context holder.
            //It tells Spring Security that the user for this
            //specific request is now authenticated, and these are their roles.
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        // 7. Continue the filter chain, regardless of the outcome.
        filterChain.doFilter(request, response);
    }
}
