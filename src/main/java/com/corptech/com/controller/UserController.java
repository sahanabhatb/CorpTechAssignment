package com.corptech.com.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.corptech.com.model.User;
import com.corptech.com.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "SignUp", tags = "SignUp")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	 	@PostMapping("/signup")
	    @ApiOperation(value = "Login", response = String.class)
	    public Boolean create(@RequestBody Map<String, String> body) throws NoSuchAlgorithmException {
	        String username = body.get("username");
	        User userExists=userRepository.findByUsername(username);
	        if (userExists!=null){

	        	 String password = body.get("password");
	 	        String encodedPassword = new BCryptPasswordEncoder().encode(password);
	 	       userExists.setUsername(username);
	 	      userExists.setPassword(encodedPassword);
	 	     userExists.setEmailid(body.get("emailid"));
	 	    userExists.setName(body.get("name"));
	 	   userExists.setRole((body.get("role")).toUpperCase());
	 	        userRepository.save(userExists);  
	 	        return true;

	        }

	        String password = body.get("password");
	        String encodedPassword = new BCryptPasswordEncoder().encode(password);
	        User user=new User();
			user.setUsername(username);
			user.setPassword(encodedPassword);
			user.setEmailid(body.get("emailid"));
			user.setName(body.get("name"));
			user.setRole(body.get("role"));
	        userRepository.save(user);
	        return true;
	    }
	 
	
}
