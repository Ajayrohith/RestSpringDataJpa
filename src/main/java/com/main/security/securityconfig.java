package com.main.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class securityconfig {

    // We use the below InMemoryUserDetailsManager for hardcoding usernames,passwords and roles
    // @Bean
    // public InMemoryUserDetailsManager userDetailsManager()
    // {

    //     UserDetails Ajay = User.builder()
    //                         .username("Ajay")
    //                         .password("{noop}test123")
    //                         .roles("Employee")
    //                         .build();

    //     UserDetails Ajeet = User.builder()
    //                         .username("Ajeet")
    //                         .password("{noop}test456")
    //                         .roles("Employee","Manager")
    //                         .build();

    //     UserDetails Susan = User.builder()
    //                         .username("Susan")
    //                         .password("{noop}test456")
    //                         .roles("Employee","Manager","Admin")
    //                         .build();
        
    //     return new InMemoryUserDetailsManager(Ajay,Ajeet,Susan);
        
    // }


    //The Below userDetailsManager is used for retrieving user and priviages from Db 

    @Bean
    public UserDetailsManager userDetailsManager (DataSource datasource)
    {
        return new JdbcUserDetailsManager(datasource);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
    {
        http.authorizeHttpRequests(configurer
            -> configurer.requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
            .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
            .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
            .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
        );

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

}
