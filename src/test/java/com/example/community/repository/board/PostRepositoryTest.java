package com.example.community.repository.board;

import com.example.community.data.category.Category;
import com.example.community.model.DAO.board.Post;
import com.example.community.model.DAO.user.User;
import com.example.community.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void deleteAllByUserId() {
        User user = new User("mmmm", "mmmm", "mmmm", "mmmm");
        userRepository.save(user);

        Post post1 = new Post("mmm", "mmm", user, Category.BUG, Category.JAVA);
        Post post2 = new Post("mmm", "mmm", user, Category.BUG, Category.JAVA);
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> findPosts = postRepository.findAllByUserId(user.getId());
        assertEquals(findPosts.size(), 2);

        postRepository.deleteAllByUserId(user.getId());
        List<Post> result = postRepository.findAllByUserId(user.getId());
        assertEquals(result.size(), 0);
    }
}