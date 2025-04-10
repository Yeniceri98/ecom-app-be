package org.application.ecomappbe.config;

import lombok.RequiredArgsConstructor;
import org.application.ecomappbe.model.AppRole;
import org.application.ecomappbe.model.Role;
import org.application.ecomappbe.model.User;
import org.application.ecomappbe.repository.RoleRepository;
import org.application.ecomappbe.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class DemoUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if roles already exist, create if they don't
        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_SELLER)));

        // Check if users exist, create if they don't
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User("admin", "admin@example.com", passwordEncoder.encode("a123"), new HashSet<>(Set.of(adminRole)));
            userRepository.save(adminUser);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User("user", "user@example.com", passwordEncoder.encode("u123"), new HashSet<>(Set.of(userRole)));
            userRepository.save(user);
        }

        if (userRepository.findByUsername("seller").isEmpty()) {
            User seller = new User("seller", "seller@example.com", passwordEncoder.encode("s123"), new HashSet<>(Set.of(sellerRole)));
            userRepository.save(seller);
        }
    }
}
