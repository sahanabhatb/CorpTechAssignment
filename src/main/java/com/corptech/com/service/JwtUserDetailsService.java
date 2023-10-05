package com.corptech.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.corptech.com.model.User;
import com.corptech.com.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        		Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }

}