package com.example.imenu_spring_project.service;

import com.example.imenu_spring_project.domain.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface CustomUserDetailsService {


    public UserDetails loadUserByUsername(final String username);


    public org.springframework.security.core.userdetails.User createUser(String username, User user);

}

