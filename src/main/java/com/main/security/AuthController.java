package com.main.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.main.security.*;
import com.main.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login( @RequestBody AuthRequest request) {

        authenticationManager.authenticate(

            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword())
        );

        return jwtUtil.generateToken(request.getUsername());
    }
}