package com.example.community.controller;

import com.example.community.dto.Comment;
import com.example.community.dto.Response;
import com.example.community.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController()
@Api(tags = {"댓글 관련 API"})
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ApiOperation(value = "테스트용")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(){
        String testString = "test_sungmintest";
        System.out.println("test=>" + testString.contains("_sungmin"));
    }

    @ApiOperation(value = "댓글 달기", notes = "글에 댓글 추가")
    @RequestMapping(value = "/create")
    public ResponseEntity<?> createComment(@RequestParam long boardId){
        Response response = new Response();
        HttpStatus status = response.getStatus();
        return new ResponseEntity<>(response, status);
    }

    @ApiOperation(value = "대댓글 달기", notes = "댓글에 대댓글 추가")
    @RequestMapping(value = "/add")
    public ResponseEntity<?> addComment(@RequestParam long boardId){
        Response response = new Response();
        HttpStatus status = response.getStatus();
        return new ResponseEntity<>(response, status);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제")
    @RequestMapping(value = "/delete")
    public ResponseEntity<?> deleteComment(@RequestParam long boardId, @RequestParam long commentId,
                                           HttpServletRequest request){
        Response response = new Response();
        Comment comment = new Comment();
        HttpStatus status;
        try {
            if(!Objects.equals(boardId, "")){ comment.setBoardId(boardId); }
            else{ comment.setCommentId(commentId); }
            response = commentService.deleteComment(comment, request);
            status = response.getStatus();
        }catch (Exception e){
            String error = e.getMessage();
            response.setMessage("Fail to Request");
            response.setData(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Delete Error =>"+error);
        }
        return new ResponseEntity<>(response, status);
    }

    @ApiOperation(value = "댓글 신고", notes = "댓글을 신고")
    @RequestMapping(value = "/report")
    public ResponseEntity<?> reportComment(@RequestParam long boardId, @RequestParam long commentId,
                                            HttpServletRequest request){
        Response response = new Response();
        HttpStatus status = response.getStatus();
        try {

        }catch (Exception e){

        }
        return new ResponseEntity<>(response, status);
    }

    @ApiOperation(value = "댓글 목록을 불러옴", notes = "글에 달린 댓글 목록을 가져옴")
    @RequestMapping(value = "/list")
    public ResponseEntity<?> searchComment(@RequestParam long boardId){
        Response response = new Response();
        HttpStatus status = response.getStatus();
        return new ResponseEntity<>(response, status);
    }

//    @ApiOperation(value = "댓글 삭제", notes = "관리자용 댓글 삭제" +
//    "일반 댓글 삭제랑 같이 사용을 할 생각을 해봐야함")
//    @RequestMapping(value = "create")
//    public ResponseEntity<?> createComment(@RequestParam long boardId){
//        Response response = new Response();
//        HttpStatus status = response.getStatus();
//        return new ResponseEntity<>(response, status);
//    }

    //리스트를 어떤 식으로 불러올지 생각을 해봐야함
    @ApiOperation(value = "댓글 총 개수 불러오기", notes = "글에 달린 댓글의 총 개수를 가져옴")
    @RequestMapping(value = "/total")
    public ResponseEntity<?> getCommentTotal(){
        Response response = new Response();
        HttpStatus status = response.getStatus();
        return new ResponseEntity<>(response, status);
    }
}
