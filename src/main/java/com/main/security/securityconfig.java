package com.main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
    {
        http.authorizeHttpRequests(configurer
            -> configurer.requestMatchers(HttpMethod.GET, "/api/employees").hasRole("Employee")
            .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("Employee")
            .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("Manager")
            .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("Admin")
        );

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

}
