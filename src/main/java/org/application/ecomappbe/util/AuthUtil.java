package org.application.ecomappbe.util;

import org.application.ecomappbe.model.User;
import org.application.ecomappbe.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUtil {
    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + authentication.getName())
        );

        return user.getEmail();
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + authentication.getName())
        );

        return user.getUserId();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + authentication.getName())
        );

        return user;
    }
}
