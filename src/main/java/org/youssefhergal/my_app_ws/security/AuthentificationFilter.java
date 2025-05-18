package org.youssefhergal.my_app_ws.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.youssefhergal.my_app_ws.SpringApplicationContext;
import org.youssefhergal.my_app_ws.controllers.UserController;
import org.youssefhergal.my_app_ws.requests.UserLoginRequest;
import org.youssefhergal.my_app_ws.services.UserService;
import org.youssefhergal.my_app_ws.services.UserServiceImpl;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthentificationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final AuthenticationManager authenticationManager;



    public AuthentificationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(SecurityContants.LOGIN_URL); // Définir l'URL de login
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
        try {
            UserLoginRequest creds = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);
            
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      FilterChain chain,
                                      Authentication authResult) {
        String userName = ((User) authResult.getPrincipal()).getUsername();
        
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityContants.EXPIRATION_TIME))
                .signWith(SecurityContants.getSigningKey())
                .compact();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");

        UserDto userDto = userService.getUser(userName);
        
        response.addHeader(SecurityContants.HEADER_STRING, 
                SecurityContants.TOKEN_PREFIX + token);
        response.addHeader("user_id", userDto.getUserId());
}
}