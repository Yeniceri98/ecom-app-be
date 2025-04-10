package org.application.ecomappbe.service;

import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.dto.RegisterRequest;
import org.application.ecomappbe.exception.UserAlreadyExistsException;
import org.application.ecomappbe.model.AppRole;
import org.application.ecomappbe.model.Role;
import org.application.ecomappbe.model.User;
import org.application.ecomappbe.repository.RoleRepository;
import org.application.ecomappbe.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException((registerRequest.getUsername()) + " is already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException((registerRequest.getEmail()) + " is already exists");
        }

        // Creating a ROLE_USER as a default role when registering a new user
        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(AppRole.ROLE_USER);
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }
}
