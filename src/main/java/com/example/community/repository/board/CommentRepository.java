package com.example.community.repository.board;

import com.example.community.model.DAO.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUserId(long userId);

    List<Comment> findAllByPostId(long postId);
}
