package com.example.community.repository.user;

import com.example.community.model.DAO.user.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

}
