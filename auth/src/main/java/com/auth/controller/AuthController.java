package com.auth.controller;


import com.auth.dto.APIResponse;
import com.auth.dto.LoginDto;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.service.AuthService;
import com.auth.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private AuthService authService;

    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private UserRepository userRepository;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }


    @PostMapping("/customer_signup")

    public ResponseEntity<APIResponse<String>> newCustomer(
            @RequestBody UserDto userDto
    ) {
        APIResponse<String> response = authService.register(userDto, "ROLE_CUSTOMER");
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/store_signup")

    public ResponseEntity<APIResponse<String>> newStore(
            @RequestBody UserDto userDto
    ) {
        APIResponse<String> response = authService.register(userDto, "ROLE_STORE");
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> loginCheck(@RequestBody LoginDto loginDto) {
        APIResponse<String> response = new APIResponse<>();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        if (authenticate.isAuthenticated()) {

            String jwtToken = jwtService.generateToken(loginDto.getUsername(), authenticate.getAuthorities().iterator().next().getAuthority());
            response.setData(jwtToken);
            response.setStatus(201);
            response.setMessage("Generated Token");
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus())) ;
        }
        response.setData("failed");
        response.setStatus(500);
        response.setMessage("Unable to authenticate");
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus())) ;
    }

    @GetMapping("/get-user")
    public User getUser(@RequestParam String username) {
        return userRepository.findByUsername(username);
    }
}
