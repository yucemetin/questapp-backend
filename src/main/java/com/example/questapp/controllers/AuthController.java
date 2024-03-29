package com.example.questapp.controllers;

import com.example.questapp.business.abstracts.UserService;
import com.example.questapp.business.requests.CreateUserRequest;
import com.example.questapp.business.requests.LoginUserRequest;
import com.example.questapp.business.responses.auth.AuthResponse;
import com.example.questapp.entities.User;
import com.example.questapp.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginUserRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = "Bearer " + jwtTokenProvider.generateJwtToken(auth);
        User user = userService.getOneUserByUserName(loginRequest.getUserName());
        return new AuthResponse(user.getId(), jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginUserRequest userRequest) {
        if (this.userService.getOneUserByUserName(userRequest.getUserName()) != null) {
            return new ResponseEntity<>(new AuthResponse(null, "Username already in use"), HttpStatus.BAD_REQUEST);
        }

        CreateUserRequest user = new CreateUserRequest();
        user.setUserName(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        this.userService.createUser(user);
        return new ResponseEntity<>(new AuthResponse(this.userService.getOneUserByUserName(user.getUserName()).getId(), "User successfully registered"), HttpStatus.CREATED);
    }
}
