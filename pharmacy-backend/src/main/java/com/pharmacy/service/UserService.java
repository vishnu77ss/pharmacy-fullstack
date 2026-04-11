package com.pharmacy.service;

import com.pharmacy.dto.UserDTO;
import com.pharmacy.entity.User;
import com.pharmacy.enums.Role;
import com.pharmacy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository cannot be null");
    }

    public User createUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setLicenseNumber(dto.getLicenseNumber());
        return userRepository.save(user);
    }

    public List<User> getAllUsers(String role) {
        if (role != null && !role.isEmpty()) {
            return userRepository.findByRole(Role.valueOf(role.toUpperCase()));
        }
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(Objects.requireNonNull(id, "id cannot be null"))
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }
}
