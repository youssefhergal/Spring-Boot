package org.youssefhergal.my_app_ws.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.youssefhergal.my_app_ws.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configuration de l'AuthenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder
            .userDetailsService(userService)
            .passwordEncoder(bCryptPasswordEncoder);
        
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        // Configuration du filtre d'authentification
        AuthentificationFilter authentificationFilter = new AuthentificationFilter(authenticationManager);
        authentificationFilter.setFilterProcessesUrl(SecurityContants.LOGIN_URL);

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers(HttpMethod.POST, SecurityContants.SIGN_UP_URL).permitAll()
                    .requestMatchers(HttpMethod.POST, SecurityContants.LOGIN_URL).permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/register").permitAll()
                    .anyRequest().authenticated();
            })
            .addFilter(authentificationFilter)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoder.encode("admin123"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}