package com.pharmacy.repository;

import com.pharmacy.entity.User;
import com.pharmacy.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
}
