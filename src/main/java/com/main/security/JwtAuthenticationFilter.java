package com.main.security;

import java.io.IOException;


import org.springframework.security.authentication.
UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.
SecurityContextHolder;

import org.springframework.security.core.userdetails.
UserDetails;

import org.springframework.security.core.userdetails.
UserDetailsService;

import org.springframework.security.web.authentication.
WebAuthenticationDetailsSource;


import org.springframework.web.filter.
OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // OncePerRequestFilter ensures the jwt validation is done only once per request
    private final JwtUtil jwtUtil; 
    private final UserDetailsService  userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,UserDetailsService  userDetailsService)
    {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    //The below method is called automatically everytime
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

    // Get Authorization header
    String authHeader = request.getHeader("Authorization");

    // Check if header is missing or does not start with "Bearer "
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);  // doFilter ensures the request is moved further to next filter/controller.
        //Without the above line the request will stay here without any further processing
        return;
    }

    // Extract JWT token (remove "Bearer ")
    String token = authHeader.substring(7);

    // Extract username from token
    String username = jwtUtil.extractUsername(token);

    // Check if username exists and user is not already authenticated
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) 
        
        /*SecurityContextHolder checks if the user is already authenticated. We proceed only if the authentication has 
        not been perfomred before*/
        {

        // Load user details from DB
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Validate JWT token
        if (jwtUtil.validateToken(token)) {

            // Create authentication object
            //The second parameter here is the password and it is always null
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            /* Add request details (IP, session, etc.)
            The below is not a mandatory part. It is used to log from which ip the request has been hit*/
            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            // Store authentication in Security Context
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }
    }

            // Continue filter chain
            filterChain.doFilter(request, response);
    }
}