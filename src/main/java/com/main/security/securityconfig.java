package com.main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class securityconfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager()
    {

        UserDetails Ajay = User.builder()
                            .username("Ajay")
                            .password("{noop}test123")
                            .roles("Employee")
                            .build();

        UserDetails Ajeet = User.builder()
                            .username("Ajeet")
                            .password("{noop}test456")
                            .roles("Employee","Manager")
                            .build();

        UserDetails Susan = User.builder()
                            .username("Susan")
                            .password("{noop}test456")
                            .roles("Employee","Manager","Admin")
                            .build();
        
        return new InMemoryUserDetailsManager(Ajay,Ajeet,Susan);
        
    }

}
