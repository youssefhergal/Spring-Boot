package org.youssefhergal.my_app_ws.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.youssefhergal.my_app_ws.SpringApplicationContext;
import org.youssefhergal.my_app_ws.requests.UserLoginRequest;
import org.youssefhergal.my_app_ws.services.UserService;
import org.youssefhergal.my_app_ws.shared.dto.UserDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthentificationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    public AuthentificationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(SecurityConstants.LOGIN_URL); // Définir l'URL de login
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
                                            Authentication authResult) throws IOException {
        String userName = ((User) authResult.getPrincipal()).getUsername();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");

        UserDto userDto = userService.getUser(userName);

        String token = Jwts.builder()
                .setSubject(userName)
                .claim("id", userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.TOKEN_SECRET), SignatureAlgorithm.HS512)
                .compact();


        response.addHeader(SecurityConstants.HEADER_STRING,
                SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("user_id", userDto.getUserId());

        response.getWriter().write("{\"token\": \"" + token + "\", \"id\": \"" + userDto.getUserId() + "\"}");

    }
}