package br.com.stormaid.controller;

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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "E-mail já cadastrado"));
        }

        Role citizenRole = roleRepository.findByName(UserRole.CITIZEN)
                .orElseThrow(() -> new RuntimeException("Role CITIZEN não encontrada"));

        User newUser = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .status(UserStatus.ACTIVE)
                .roles(new HashSet<>(Set.of(citizenRole)))
                .build();

        User savedUser = userRepository.save(newUser);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()),
                savedUser.getStatus()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        var userInfo = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()),
                user.getStatus()
        );

        LoginResponse loginResponse = new LoginResponse(token, "Bearer", 7200L, userInfo);

        return ResponseEntity.ok(loginResponse);
    }
}