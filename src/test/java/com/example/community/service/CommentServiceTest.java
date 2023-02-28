package com.example.community.service;

import com.example.community.data.category.Category;
import com.example.community.data.report.ReportType;
import com.example.community.model.DAO.board.Comment;
import com.example.community.model.DAO.board.Post;
import com.example.community.model.DAO.report.CommentReport;
import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.comment.CommentDeleteDTO;
import com.example.community.model.DTO.comment.CommentReportDTO;
import com.example.community.model.DTO.comment.CommentWriteDTO;
import com.example.community.repository.board.CommentRepository;
import com.example.community.repository.board.PostRepository;
import com.example.community.repository.report.CommentReportRepository;
import com.example.community.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentReportRepository commentReportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    User user = new User("aa", "aa", "aa", "aa");
    Post post;
    String user1UserId = "bbbb";

    @BeforeTestClass
    void init(){
        commentService = new CommentService(commentRepository, commentReportRepository, postRepository, userRepository);
    }

    @BeforeEach
    void beforeInit(){
        userRepository.save(user);

        post = new Post("aa", "aa", user, Category.BUG, Category.JAVA);
        postRepository.save(post);
    }

    @AfterEach
    void afterInit(){
        postRepository.findAllByUserId(user.getId()).iterator()
                .forEachRemaining( post -> {
                    postRepository.delete(post);
                });

        commentRepository.findAllByUserId(user.getId()).iterator()
                .forEachRemaining(comment -> {
                    commentRepository.delete(comment);
                });

        commentReportRepository.findAllByReporter(user).iterator()
                        .forEachRemaining(report -> {
                            commentReportRepository.delete(report);
                        });

        userRepository.delete(user);

    }

    @Nested
    @DisplayName("댓글 작성")
    class writeComment {

        @Test
        @DisplayName("성공 로직")
        void success1() {
            CommentWriteDTO commentWriteDTO = new CommentWriteDTO(user.getId(), post.getId(), "감사합니다.");

            assertDoesNotThrow(() -> {
                commentService.writeComment(commentWriteDTO);
            });

            List<Comment> comments = commentRepository.findAllByPostId(post.getId());

            assertEquals(comments.size(), 1);
            assertEquals(comments.get(0).getText(), commentWriteDTO.getText());
        }

        @Test
        @DisplayName("성공 로직(내용 null일 때)")
        void success2() {
            String defaultText = "너무 좋아요:)";

            CommentWriteDTO commentWriteDTO = new CommentWriteDTO(user.getId(), post.getId(), null);

            assertDoesNotThrow(() -> {
                commentService.writeComment(commentWriteDTO);
            });

            List<Comment> comments = commentRepository.findAllByPostId(post.getId());

            assertEquals(comments.size(), 1);
            assertEquals(comments.get(0).getText(), defaultText);
        }

        @Test
        @DisplayName("실패 로직(존재하지 않는 user or post)")
        void fail1() {
            assertThrows(NoSuchElementException.class, () -> {
                CommentWriteDTO commentWriteDTO = new CommentWriteDTO(user.getId(), post.getId() + 1, "감사합니다");

                commentService.writeComment(commentWriteDTO);
            });

            assertThrows(NoSuchElementException.class, () -> {
                CommentWriteDTO commentWriteDTO = new CommentWriteDTO(user.getId()+1, post.getId(), "감사합니다");

                commentService.writeComment(commentWriteDTO);
            });
        }

        @Test
        @DisplayName("실패 로직(user, post 중 하나라도 null)")
        void fail2() {
            Throwable exception1 = assertThrows(IllegalArgumentException.class, () -> {
                CommentWriteDTO commentWriteDTO = new CommentWriteDTO(null, post.getId() + 1, "감사합니다");

                commentService.writeComment(commentWriteDTO);
            });

            Throwable exception2 = assertThrows(IllegalArgumentException.class, () -> {
                CommentWriteDTO commentWriteDTO = new CommentWriteDTO(user.getId(), null, "감사합니다");

                commentService.writeComment(commentWriteDTO);
            });

            assertEquals(exception1.getMessage(), "variable can not be null");
            assertEquals(exception2.getMessage(), "variable can not be null");
        }

    }

    @Nested
    @DisplayName("댓글 모두 불러오기")
    class comments {

        @Test
        @DisplayName("성공 로직")
        void success() {
            Post post1 = new Post("aa", "aaa", user, Category.BUG, Category.C);

            postRepository.save(post1);

            List<Comment> comments = new ArrayList<>();
            comments.add(new Comment(user, post, null));
            comments.add(new Comment(user, post, null));
            comments.add(new Comment(user, post, null));
            comments.add(new Comment(user, post, null));
            comments.add(new Comment(user, post1, null));
            comments.add(new Comment(user, post1, null));
            commentRepository.saveAll(comments);

            assertDoesNotThrow(() -> {
                List<Comment> findComments = commentService.comments(post.getId());

                assertEquals(findComments.size(), 4);
            });

        }

        @Test
        @DisplayName("실패 로직(post id가 없음)")
        void fail() {
            Comment comment1 = new Comment(user, post, null);
            Comment comment2 = new Comment(user, post, null);

            List<Comment> comments = new ArrayList<>();
            comments.add(comment1);
            comments.add(comment2);

            commentRepository.saveAll(comments);

            assertThrows(NoSuchElementException.class, () -> {
                commentService.comments(post.getId()+1);
            });
        }

    }

    @Nested
    @DisplayName("댓글 삭제")
    class deleteComment {

        @Test
        @DisplayName("성공 로직")
        void success() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            CommentDeleteDTO commentDeleteDTO = new CommentDeleteDTO(user.getId(), post.getId(), comment.getId());
            assertDoesNotThrow(() -> {
                commentService.deleteComment(commentDeleteDTO);
            });

            Comment result = commentRepository.findById(comment.getId())
                    .orElse(null);
            assertNull(result);
        }

        @Test
        @DisplayName("실패 로직(userId 불일치)")
        void fail1() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            User user1 = new User("mm", "mmmm", "aa", "aaaa");
            userRepository.save(user1);

            CommentDeleteDTO commentDeleteDTO = new CommentDeleteDTO(user1.getId(), post.getId(), comment.getId());
            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                commentService.deleteComment(commentDeleteDTO);
            });
            assertEquals(exception.getMessage(), "user not match for comment");

            userRepository.delete(user1);
        }

        @Test
        @DisplayName("실패 로직(없는 commentId)")
        void fail2() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            CommentDeleteDTO commentDeleteDTO = new CommentDeleteDTO(user.getId(), post.getId(), comment.getId() + 1);
            assertThrows(NoSuchElementException.class, () -> {
                commentService.deleteComment(commentDeleteDTO);
            });
        }

        @Test
        @DisplayName("실패 로직(없는 postId)")
        void fail3() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            CommentDeleteDTO commentDeleteDTO = new CommentDeleteDTO(user.getId(), post.getId() + 1, comment.getId());
            assertThrows(NoSuchElementException.class, () -> {
                commentService.deleteComment(commentDeleteDTO);
            });
        }

        @Test
        @DisplayName("실패 로직(post에 해당 comment 없음)")
        void fail4() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            Post post1 = new Post("aa", "aaa", user, Category.BUG, Category.JAVA);
            postRepository.save(post1);

            Comment comment1 = new Comment(user, post1, null);
            commentRepository.save(comment1);

            CommentDeleteDTO commentDeleteDTO = new CommentDeleteDTO(user.getId(), post.getId(), comment1.getId());
            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                commentService.deleteComment(commentDeleteDTO);
            });
            assertEquals(exception.getMessage(), "comment not found with post");
        }

    }

    @Nested
    @DisplayName("댓글 신고")
    class reportComment {

        @Test
        @DisplayName("성공 로직")
        void success() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            CommentReportDTO commentReportDTO = new CommentReportDTO(
                    user.getId(), post.getId(), comment.getId(), ReportType.ADVERTISING, "ㅁㅁ"
            );
            commentService.reportComment(commentReportDTO);

            List<CommentReport> reports = commentReportRepository.findAllByReporter(user);
            assertEquals(1, reports.size());

            Comment findComment = commentRepository.findById(comment.getId())
                    .orElse(null);
            assertNotNull(findComment);
        }

        @Test
        @DisplayName("실패 로직(해당 post에 해당 comment 없음)")
        void fail1() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            Post post1 = new Post("aa", "aa", user, Category.BUG, Category.C);
            postRepository.save(post1);

            CommentReportDTO commentReportDTO = new CommentReportDTO(user.getId(), post1.getId(), comment.getId(), ReportType.ADVERTISING, "신고합니다");

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                commentService.reportComment(commentReportDTO);
            });
            assertEquals(exception.getMessage(), "comment not found with post");
        }

        @Test
        @DisplayName("실패 로직(해당 post 없음)")
        void fail2() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            CommentReportDTO commentReportDTO = new CommentReportDTO(user.getId(), post.getId() + 1, comment.getId(), ReportType.ADVERTISING, "신고합니다");

            assertThrows(NoSuchElementException.class, () -> {
                commentService.reportComment(commentReportDTO);
            });
        }

        @Test
        @DisplayName("실패 로직(해당 user 없음)")
        void fail3() {
            Comment comment = new Comment(user, post, null);
            commentRepository.save(comment);

            CommentReportDTO commentReportDTO = new CommentReportDTO(user.getId() + 5, post.getId(), comment.getId(), ReportType.ADVERTISING, "신고합니다");

            assertThrows(NoSuchElementException.class, () -> {
                commentService.reportComment(commentReportDTO);
            });
        }

    }

}