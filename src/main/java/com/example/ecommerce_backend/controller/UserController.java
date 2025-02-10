package com.example.ecommerce_backend.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_backend.entity.User;
import com.example.ecommerce_backend.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager customAuthenticationManager;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
        @RequestBody User user,
        @RequestHeader("Authorization") String authHeader
    ){
        try {
            String[] credentials = extractCredentials(authHeader);
            String requestUsername = credentials[0];
            String requestPassword = credentials[1];

            Authentication authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestPassword, requestUsername)
            );
            if(authentication.isAuthenticated()){
                User oldUser = userService.findByUsername(requestUsername);
                if(oldUser != null){
                    oldUser.setUsername(user.getUsername());
                    oldUser.setPassword(user.getPassword());
                    userService.saveEntry(oldUser);
                    return new ResponseEntity<>(oldUser, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    private String[] extractCredentials(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            return credentials.split(":", 2);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
