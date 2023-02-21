package com.example.community.repository.user;

import com.example.community.model.DAO.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
