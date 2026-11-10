package br.com.stormaid.service;

import br.com.stormaid.dto.request.LoginRequest;
import br.com.stormaid.dto.request.RegisterRequest;
import br.com.stormaid.dto.response.LoginResponse;
import br.com.stormaid.dto.response.UserResponse;
import br.com.stormaid.entity.Role;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.UserRole;
import br.com.stormaid.enums.UserStatus;
import br.com.stormaid.repository.RoleRepository;
import br.com.stormaid.repository.UserRepository;
import br.com.stormaid.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, TokenService tokenService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role citizenRole = roleRepository.findByName(UserRole.CITIZEN)
                .orElseThrow(() -> new RuntimeException("Default role CITIZEN not found"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRoles(Collections.singleton(citizenRole));

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()),
                savedUser.getStatus()
        );
    }

    public LoginResponse login(LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();

        String token = tokenService.generateToken(user);

        var userInfo = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()),
                user.getStatus()
        );

        return new LoginResponse(token, "Bearer", 7200L, userInfo);
    }
}