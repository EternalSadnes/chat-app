package com.eternal.chatapp.service.impl;

import com.eternal.chatapp.model.CustomUsrDetails;
import com.eternal.chatapp.model.User;
import com.eternal.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email = " + email + " not exist!"));
        return new CustomUsrDetails(user);
    }
}
