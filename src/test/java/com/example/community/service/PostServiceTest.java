package com.example.community.service;

import com.example.community.data.category.Category;
import com.example.community.model.DAO.board.Post;
import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.post.PostEditDTO;
import com.example.community.model.DTO.post.PostWriteDTO;
import com.example.community.repository.board.PostRepository;
import com.example.community.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;

    User user = new User("aa", "Aaa", "aa", "aa");
    User user1 = new User("bbb", "bbb", "bbb", "bbb");
    Post post;


    String board1Title = "board1";
    String board2Title = "board2";

    @BeforeTestClass
    void init() {
        postService = new PostService(postRepository);
    }
    @BeforeEach
    void beforeClean() {
        userRepository.save(user);
        userRepository.save(user1);

        post = new Post("aa", "aa", user, Category.BUG, Category.JAVA);
        postRepository.save(post);
    }
    @AfterEach
    void afterInit() {
        postRepository.findAllByUserId(user.getId())
                        .iterator().forEachRemaining(
                                post -> {
                                    postRepository.delete(post);
                                });
        postRepository.findAllByUserId(user1.getId())
                .iterator().forEachRemaining(
                        post -> {
                            postRepository.delete(post);
                        });
        userRepository.delete(user);
        userRepository.delete(user1);;
    }

    @Nested
    @DisplayName("글 목록 불러오기")
    class posts{

        Pageable request = PageRequest.of(0, 10);

        @Test
        @DisplayName("성공 로직")
        void success() {
            List<Post> boards = new ArrayList<>();

            boards.add(new Post(board1Title, board1Title, user, Category.BUG, Category.JAVA));
            boards.add(new Post(board2Title, board2Title, user, Category.BUG, Category.JAVA));
            boards.add(new Post(board2Title, board2Title, user1, Category.BUG, Category.JAVA));

            postRepository.saveAll(boards);

            assertDoesNotThrow(() -> {
                List<Post> posts = postService.posts(user, request);

                assertEquals(posts.size(), 1);
                posts.iterator()
                        .forEachRemaining(post -> {
                            User resultUser = userRepository.findById(post.getUser().getId())
                                    .orElse(null);
                            assertNotNull(resultUser);

                            assertEquals(user1.getId(), resultUser.getId());
                        });
            });
        }

        @Test
        @DisplayName("실패 로직(매개 변수가 안넘어감)")
        void fail() {
            Throwable exception = assertThrows(NullPointerException.class, () -> {
                postService.posts(user, null);
            });
            assertEquals(exception.getMessage(), "request can not be null");

            Throwable exception2 = assertThrows(NullPointerException.class, () -> {
                postService.posts(null, request);
            });
            assertEquals(exception2.getMessage(), "request can not be null");
        }
    }

    @Nested
    @DisplayName("글 작성")
    class writePost{

        @Test
        @DisplayName("성공 로직")
        void success() {

            PostWriteDTO postWriteDTO = new PostWriteDTO("aa", "aa", user, Category.BUG, Category.JAVA);

            assertDoesNotThrow(() -> {
                Post result = postService.writePost(postWriteDTO);

                Post findPost = postRepository.findById(result.getId())
                        .orElse(null);
                assertNotNull(findPost);

                assertEquals(findPost.getTitle(), postWriteDTO.getTitle());
                assertEquals(findPost.getText(), postWriteDTO.getText());
            });
        }

        @Test
        @DisplayName("실패 로직(제목, 내용, 작성자, 카테고리 중 하나라도 빠짐)")
        void fail1() throws IllegalAccessException {

            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                PostWriteDTO post = new PostWriteDTO(null, "aa", user, Category.BUG, Category.JAVA);
                postService.writePost(post);
            });
            assertEquals(exception.getMessage(), "post variable can not be null");

            Throwable exception1 = assertThrows(IllegalArgumentException.class, () -> {
                PostWriteDTO post = new PostWriteDTO("aa", null, user, Category.BUG, Category.JAVA);
                postService.writePost(post);
            });
            assertEquals(exception1.getMessage(), "post variable can not be null");

            Throwable exception2 = assertThrows(IllegalArgumentException.class, () -> {
                PostWriteDTO post = new PostWriteDTO("aa", "aa", null, Category.BUG, Category.JAVA);
                postService.writePost(post);
            });
            assertEquals(exception2.getMessage(), "post variable can not be null");

            Throwable exception3 = assertThrows(IllegalArgumentException.class, () -> {
                PostWriteDTO post = new PostWriteDTO("aa", "aa", user, null, Category.JAVA);
                postService.writePost(post);
            });
            assertEquals(exception3.getMessage(), "post variable can not be null");

            Throwable exception4 = assertThrows(IllegalArgumentException.class, () -> {
                PostWriteDTO post = new PostWriteDTO("aa", "aa", user, Category.BUG, null);
                postService.writePost(post);
            });
            assertEquals(exception4.getMessage(), "post variable can not be null");

            Throwable exception5 = assertThrows(IllegalArgumentException.class, () -> {
                PostWriteDTO post = new PostWriteDTO(null, "aa", null, Category.BUG, Category.JAVA);
                postService.writePost(post);
            });
            assertEquals(exception5.getMessage(), "post variable can not be null");
        }

    }

    @Nested
    @DisplayName("글 수정")
    class editPost{

        String changeTitle = "bb";
        PostEditDTO postEditDTO = new PostEditDTO(changeTitle, "aa", Category.BUG, Category.JAVA);

        @Test
        @DisplayName("성공 로직")
        void success() {
            assertDoesNotThrow(() -> {
                postService.editPost(post.getId(), postEditDTO);
            });

            Post result = postRepository.findById(post.getId())
                    .orElse(null);
            assertNotNull(result);

            assertEquals(changeTitle, result.getTitle());
            assertEquals(postEditDTO.getText(), result.getText());
        }

        @Test
        @DisplayName("실패 로직(해당하는 ID의 글이 없음)")
        void fail1() {
            assertThrows(NoSuchElementException.class, () -> {
                postService.editPost(0, postEditDTO);
            });
        }

        @Test
        @DisplayName("실패 로직(null이 존재함)")
        void fail2() {
            Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                PostEditDTO postEditDTO = new PostEditDTO(null, "aa", Category.BUG, Category.JAVA);
                postService.editPost(post.getId(), postEditDTO);
            });
            assertEquals(exception.getMessage(), "post variable can not be null");
        }

    }

    @Nested
    @DisplayName("글 읽기")
    class detail{

        @Test
        @DisplayName("성공 로직")
        void success() {
            assertDoesNotThrow(() -> {
                Post postDetail = postService.detail(post.getId());

                assertEquals(post.getTitle(), postDetail.getTitle());
                assertEquals(post.getText(), postDetail.getText());
            });
        }

        @Test
        @DisplayName("실패 로직(해당 ID가 존재하지 않음)")
        void fail() {
            assertThrows(NoSuchElementException.class, () -> {
                postService.detail(post.getId()+1);
            });
        }

    }
}