package ru.codeline.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.codeline.models.user.Credential;
import ru.codeline.models.user.Role;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CredentialRepository;
import ru.codeline.repositories.UserRepository;

@Component
public class AdminCredentials implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Override
    public void run(String... args) {
        User adminUser = new User();
        adminUser.setFirstName("User");
        adminUser.setLastName("Admin");
        adminUser.setEmail("admin@example.com");
        userRepository.save(adminUser);

        Credential adminCredential = new Credential();
        adminCredential.setUser(adminUser);

        String encodedPassword = passwordEncoder.encode("admin123"); // Encode password
        adminCredential.setPassword(encodedPassword);

        adminCredential.setRole(Role.ADMIN);
        credentialRepository.save(adminCredential);
    }
}
