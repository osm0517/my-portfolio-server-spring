package com.example.community.repository.user;

import com.example.community.model.DAO.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByUserId() {
        User user = new User("testUserId", "testPassword", "testName", "testEmail");

        userRepository.save(user);

        User findUser = userRepository.findByUserId("testUserId")
                .orElse(null);

        assertThat(findUser).isNotNull();
        assertThat(findUser).usingRecursiveComparison()
                .isEqualTo(user);
    }
}