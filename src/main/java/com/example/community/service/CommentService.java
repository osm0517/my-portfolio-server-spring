package com.example.community.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writeComment(CommentWriteDTO commentWriteDTO) throws IllegalArgumentException{
        try{
            if(commentWriteDTO.hasNull()){
                throw new IllegalArgumentException("variable can not be null");
            }else{
                Long postId = commentWriteDTO.getPostId();
                Long userId = commentWriteDTO.getUserId();
                String text = commentWriteDTO.getText();

                Post findPost = postRepository.findById(postId)
                        .orElseThrow(NoSuchElementException::new);
                User findUser = userRepository.findById(userId)
                        .orElseThrow(NoSuchElementException::new);

                Comment comment = new Comment(findUser, findPost, text);

                commentRepository.save(comment);
            }
        }catch (IllegalAccessException e){
            log.error(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> comments(long postId){

        postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);

        return commentRepository.findAllByPostId(postId);
    }

    @Transactional()
    public void deleteComment(CommentDeleteDTO commentDeleteDTO) throws IllegalArgumentException{

        Long userId = commentDeleteDTO.getUserId();
        Long postId = commentDeleteDTO.getPostId();
        Long commentId = commentDeleteDTO.getCommentId();

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()){
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(NoSuchElementException::new);

            Post post = optionalPost.get();

            if(post.getComments().contains(comment)){

                User user = userRepository.findById(userId)
                        .orElseThrow(NoSuchElementException::new);

                if(comment.getUser().equals(user)){
                    System.out.println("aaa");
                    commentRepository.delete(comment);
                }else{
                    throw new IllegalArgumentException("user not match for comment");
                }

            }else{
                throw new IllegalArgumentException("comment not found with post");
            }
        }else{
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public void reportComment(CommentReportDTO commentReportDTO){
        Long userId = commentReportDTO.getUserId();
        Long postId = commentReportDTO.getPostId();
        Long commentId = commentReportDTO.getCommentId();

        Optional<Post> optionalPost = postRepository.findById(postId);

        if(optionalPost.isPresent()){
            Post post = optionalPost.get();

            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(NoSuchElementException::new);

            if(post.getComments().contains(comment)){
                User reporter = userRepository.findById(userId)
                        .orElseThrow(NoSuchElementException::new);

                CommentReport commentReport = new CommentReport(
                        comment, reporter, commentReportDTO.getReportType(), commentReportDTO.getText()
                );

                commentReportRepository.save(commentReport);
            }else{
                throw new IllegalArgumentException("comment not found with post");
            }
        }else{
            throw new NoSuchElementException();
        }

    }

}
