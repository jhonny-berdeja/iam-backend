package com.jb.iam_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String ALL = "*";
    private static final String ROUTE_ALL = "/**";
    private static final String ROLE_ADMIN = "admin";
    private static final String MESSAGE_UNAUTHORIZAD = "Unauthorized IAM";

    @Value("${iam.admin.user}")
    private String adminUser;

    @Value("${iam.admin.password}")
    private String adminPassword;

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.authorizeHttpRequests(
            auth -> auth.anyRequest().authenticated()
        );
        http.httpBasic(); 
        http.exceptionHandling().authenticationEntryPoint(
            (request, response, authException) -> {
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    MESSAGE_UNAUTHORIZAD
                );
            }
        );
        http.csrf().disable(); 
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(ALL);
        corsConfiguration.addAllowedMethod(ALL); 
        corsConfiguration.addAllowedHeader(ALL); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ROUTE_ALL, corsConfiguration);
        
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        String password = passwordEncoder().encode(adminPassword);
        UserDetails user = User.withUsername(adminUser)
            .password(password)
            .roles(ROLE_ADMIN)
            .build();

        return new InMemoryUserDetailsManager(user);
    }

}
