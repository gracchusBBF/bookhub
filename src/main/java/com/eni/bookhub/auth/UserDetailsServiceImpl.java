package com.eni.bookhub.auth;

import com.eni.bookhub.BO.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        return new UserDetailsImpl(user);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        com.eni.bookhub.BO.User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException(
//                        "Aucun utilisateur trouvé avec l'email : " + username
//                ));
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .authorities("ROLE_" + user.getUserRole().getRoleName())
//                .build();
//    }
}
