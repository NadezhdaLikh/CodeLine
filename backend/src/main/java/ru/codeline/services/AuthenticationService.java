package ru.codeline.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.codeline.dto.regAndAuth.AuthenticationRequest;
import ru.codeline.dto.regAndAuth.AuthenticationResponse;
import ru.codeline.dto.regAndAuth.RegisterRequest;
import ru.codeline.models.user.Credential;
import ru.codeline.models.user.Role;
import ru.codeline.models.user.User;
import ru.codeline.repositories.CredentialRepository;
import ru.codeline.repositories.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
        userRepository.save(user);

        User userEmail = userRepository.findByEmail(request.getEmail());
        var pass = Credential.builder()
                .user(userEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();
        credentialRepository.save(pass);

        var jwtToken = jwtService.generateToken(Map.of("role", pass.getRole().toString()), pass);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var pass = credentialRepository.findByUserEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(Map.of("role", pass.getRole().toString()), pass);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
