package com.example.community.repository.board;

import com.example.community.model.DAO.board.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUserIdNot(long userId, Pageable pageable);

    Optional<Post> findByUserId(long userId);

    Optional<Post> findByTitle(String title);

    List<Post> findAllByUserId(long userId);

    void deleteAllByUserId(long userId);
}
