package com.main.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



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

    // @Bean
    // public UserDetailsManager userDetailsManager (DataSource datasource)
    // {
    //     return new JdbcUserDetailsManager(datasource);
    // }

    //Use the below code to get the userdetails and roles from a custom db Table
    @Bean
    public UserDetailsManager userDetailsManager (DataSource dataSource)
    {
        JdbcUserDetailsManager theUserDetails = new JdbcUserDetailsManager(dataSource);

        theUserDetails.setUsersByUsernameQuery("select user_name,pw,enabled from Valid_Users where user_name=?");
        theUserDetails.setAuthoritiesByUsernameQuery("select user_name,authority from  Valid_Authorities where user_name = ?");

        return theUserDetails;
    }


    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http)
    // {
    //     http.authorizeHttpRequests(configurer
    //         -> configurer.requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
    //         .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
    //         .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
    //         .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
    //     );

    //     http.httpBasic(Customizer.withDefaults());

    //     http.csrf(csrf -> csrf.disable());
    //     return http.build();
    // }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {

            return new JwtAuthenticationFilter( jwtUtil,userDetailsService);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationFilter jwtAuthenticationFilter) 
        throws Exception 
        {

            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")

                    .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")

                    .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

                 http.csrf(csrf -> csrf.disable());

            return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager( AuthenticationConfiguration config)
        throws Exception
        {

            return config.getAuthenticationManager();
        }

}
