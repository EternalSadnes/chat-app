package com.eternal.chatapp.service.impl;

import java.util.Set;

import com.eternal.chatapp.dto.AuthRequestDto;
import com.eternal.chatapp.exception.RoleNotFoundException;
import com.eternal.chatapp.exception.UserAlreadyExistException;
import com.eternal.chatapp.model.Role;
import com.eternal.chatapp.model.User;
import com.eternal.chatapp.repository.RoleRepository;
import com.eternal.chatapp.repository.UserRepository;
import com.eternal.chatapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(AuthRequestDto authRequestDto) throws UserAlreadyExistException {
        if (isUserExist(authRequestDto.getUsername())) {
            throw UserAlreadyExistException.fromUsername(authRequestDto.getUsername());
        }
        Role userRole = roleRepository.findByRoleName("SCOPE_usr")
                .orElseThrow(() -> RoleNotFoundException.fromRoleName("USER"));

        User userToRegister = new User();
        userToRegister.setEmail(authRequestDto.getUsername());
        userToRegister.setPassword(encodePassword(authRequestDto.getPassword()));
        userToRegister.setRoles(Set.of(userRole));
        return userRepository.save(userToRegister);
    }

    @Override
    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
