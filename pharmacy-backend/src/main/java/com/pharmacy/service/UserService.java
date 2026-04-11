package com.pharmacy.service;

import com.pharmacy.dto.UserDTO;
import com.pharmacy.entity.User;
import com.pharmacy.enums.Role;
import com.pharmacy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }
}
