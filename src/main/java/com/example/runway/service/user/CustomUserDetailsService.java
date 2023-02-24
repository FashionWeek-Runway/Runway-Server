package com.example.runway.service.user;

import com.example.runway.domain.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface CustomUserDetailsService {


    UserDetails loadUserByUsername(final String username);


    org.springframework.security.core.userdetails.User createUser(String username, User user);

}

