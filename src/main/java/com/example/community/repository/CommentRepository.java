package com.example.community.repository;

import com.example.community.dto.Comment;
import com.example.community.dto.Report;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentRepository {

//    insert
    //글에 댓글을 작성함
    int createComment(Comment comment);
//    //댓글에 대댓글을 작성함
//    int addComment(Comment comment);
    //댓글을 신고함
    int reportComment(Report report);
//    select
    //글의 댓글 목록을 가져옴
    List<Comment> selectCommentById(long boardId);
    //글의 총 개수를 가져옴
    int commentTotal(long boardId);

    List<Integer> selectIdByBoardId(long boardId);
//    update

//    delete
    //내 댓글을 삭제함
    int deleteComment(int commentId);
//    //관리자가 댓글을 삭제함
//    int deleteUserComment(Comment comment);
}
