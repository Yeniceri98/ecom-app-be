package org.application.ecomappbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.LoginRequest;
import org.application.ecomappbe.dto.LoginResponse;
import org.application.ecomappbe.dto.RegisterRequest;
import org.application.ecomappbe.security.jwt.JwtUtils;
import org.application.ecomappbe.security.user.EcomUserDetails;
import org.application.ecomappbe.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // String jwtToken = jwtUtils.generateJwtToken(authentication);     // Token Based

        EcomUserDetails ecomUserDetails = (EcomUserDetails) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(ecomUserDetails);     // Cookie Based

        List<String> roles = ecomUserDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // return ResponseEntity.ok(new LoginResponse(ecomUserDetails.getId(), ecomUserDetails.getUsername(), jwtToken, roles));

        LoginResponse loginResponse = new LoginResponse(ecomUserDetails.getId(), ecomUserDetails.getUsername(), roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(loginResponse);
    }
}
