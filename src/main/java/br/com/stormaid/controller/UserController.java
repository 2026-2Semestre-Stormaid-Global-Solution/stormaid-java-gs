package br.com.stormaid.controller;

import br.com.stormaid.dto.response.UserResponse;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.UserStatus;
import br.com.stormaid.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getMyAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(toResponse(user));
    }

    @PutMapping("/users/me")
    @Transactional
    public ResponseEntity<UserResponse> updateMyAccount(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();

        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());

        userRepository.save(user);
        return ResponseEntity.ok(toResponse(user));
    }

    @DeleteMapping("/users/me")
    @Transactional
    public ResponseEntity<Void> deactivateMyAccount(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow();

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/internal/users")
    @PreAuthorize("hasRole('REGULATOR')")
    public ResponseEntity<Page<UserResponse>> listUsers(Pageable pageable) {
        Page<UserResponse> page = userRepository.findAll(pageable).map(this::toResponse);
        return ResponseEntity.ok(page);
    }

    private UserResponse toResponse(User user) {
        var roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                  .map(r -> r.getName().name())
                  .collect(java.util.stream.Collectors.toSet())
                : java.util.Set.<String>of();

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                roleNames,
                user.getStatus()
        );
    }

    public record UpdateUserRequest(
            @NotBlank @Size(min = 3, max = 150) String fullName,
            @NotBlank @Size(max = 20) String phoneNumber
    ) {}
}