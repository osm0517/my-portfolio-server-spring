package com.example.community.repository.report;

import com.example.community.data.category.Category;
import com.example.community.model.DAO.board.Comment;
import com.example.community.model.DAO.board.Post;
import com.example.community.model.DAO.report.CommentReport;
import com.example.community.model.DAO.user.User;
import com.example.community.repository.board.CommentRepository;
import com.example.community.repository.board.PostRepository;
import com.example.community.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentReportRepositoryTest {

    @Autowired
    CommentReportRepository commentReportRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void findAllByUserIdTest() {
        User user = new User("aa", "aa", "aa", "aa");
        userRepository.save(user);

        Post post = new Post("aa", "aa", user, Category.BUG, Category.C);
        postRepository.save(post);

        Comment comment = new Comment(user, post, null);
        commentRepository.save(comment);

        CommentReport commentReport = new CommentReport(comment, user, "aa", "aa");
        commentReportRepository.save(commentReport);

        assertDoesNotThrow(() -> {
            List<CommentReport> reports = commentReportRepository.findAllByReporter(user);
            assertEquals(reports.size(), 1);
        });
    }
}