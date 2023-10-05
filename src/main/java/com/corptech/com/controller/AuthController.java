package com.corptech.com.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.corptech.com.config.JWT.JwtToken;
import com.corptech.com.model.JwtRequest;
import com.corptech.com.model.JwtResponse;
import com.corptech.com.model.User;
import com.corptech.com.repository.UserRepository;
import com.corptech.com.service.JwtUserDetailsService;

import org.springframework.security.authentication.AuthenticationManager;

@RestController
@CrossOrigin
@Api(value = "Login", tags = "Login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    
    @Autowired
	UserRepository usersRepository;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ApiOperation(value = "Login", response = String.class)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {


        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        
        final UserDetails userDetails = jwtUserDetailsService

                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtToken.generateToken(userDetails);
        
        User userExists=usersRepository.findByUsername(authenticationRequest.getUsername());
        userExists.setJwt_token(token);
        usersRepository.save(userExists);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    private void authenticate(String username, String password) throws Exception {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {

            throw new Exception("USER_DISABLED", e);

        } catch (BadCredentialsException e) {

            throw new Exception("INVALID_CREDENTIALS", e);

        }

    }

}
