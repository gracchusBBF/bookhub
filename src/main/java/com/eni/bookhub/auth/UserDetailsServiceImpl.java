package com.eni.bookhub.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    // injecter un user repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
        // recupérer dans user repository un user(username -> mail)
        // retourner userdetailsimpl(user) -> servira à spring security

    }
}
