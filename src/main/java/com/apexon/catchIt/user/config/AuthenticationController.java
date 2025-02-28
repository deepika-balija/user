package com.apexon.catchIt.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationProvider authenticationProvider;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest)
    {
        try {
            System.out.println("Login request received for user: " + authRequest.getUserName());
           /* String encodedPass=passwordEncoder.encode(authRequest.getPassword());
            System.out.println("encodedpass"+encodedPass);
            authRequest.setPassword(encodedPass);*/
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword()));
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(authRequest.getUserName());
        String token=jwtUtility.generateToken(userDetails);
        System.out.println("jwtToken"+token);
        return  ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

}
