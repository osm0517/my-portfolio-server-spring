package com.example.community.controller;

import com.example.community.dto.Comment;
import com.example.community.dto.Report;
import com.example.community.dto.Response;
import com.example.community.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController()
@Api(tags = {"댓글 관련 API"})
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ApiOperation(value = "테스트용")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void test(@RequestBody Comment comment){

        System.out.println("test=>" + comment);
    }

    //지금 생각으로는 완벽
    @ApiOperation(value = "댓글 달기", notes = "글에 댓글 추가")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createComment(@RequestBody Comment comment){
        Response response = new Response();
        HttpStatus status;
        try {
            response = commentService.createComment(comment);
            status = response.getStatus();
        }catch (Exception e){
            String error = e.getMessage();
            response.setMessage("Fail to Request");
            response.setData(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Create Error =>"+error);
        }
        return new ResponseEntity<>(response, status);
    }

//    @ApiOperation(value = "대댓글 달기", notes = "댓글에 대댓글 추가")
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    public ResponseEntity<?> addComment(@RequestBody Comment comment){
//        Response response = new Response();
//        HttpStatus status;
//        try {
//            response = commentService.addComment(comment);
//            status = response.getStatus();
//        }catch (Exception e){
//            String error = e.getMessage();
//            response.setMessage("Fail to Request");
//            response.setData(error);
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            System.out.println("Create Error =>"+error);
//        }
//        return new ResponseEntity<>(response, status);
//    }

    //지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제할 때 본인의 댓글만 삭제해야하는데" +
            "클라이언트에서 본인 댓글에만 삭제 버튼이 나온다는 가정하고 만듦")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@RequestParam long boardId, @RequestParam int commentId,
                                           HttpServletRequest request){
        Response response = new Response();
        HttpStatus status;
        try {
            response = commentService.deleteComment(boardId, commentId, request);
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

    //지금 생각으로는 완벽
    @ApiOperation(value = "댓글 신고", notes = "댓글을 신고")
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseEntity<?> reportComment(Report report){
        Response response = new Response();
        HttpStatus status;
        try {
            response = commentService.reportComment(report);
            status = response.getStatus();
        }catch (Exception e){
            String error = e.getMessage();
            response.setMessage("Fail to Request");
            response.setData(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Report Error =>"+error);
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로는 완벽
    @ApiOperation(value = "댓글 목록을 불러옴", notes = "글에 달린 댓글 목록을 가져옴")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<?> searchComment(@RequestParam long boardId){
        Response response = new Response();
        HttpStatus status;
        try {
            response = commentService.selectCommentById(boardId);
            status = response.getStatus();
        }catch (Exception e){
            String error = e.getMessage();
            response.setMessage("Fail to Request");
            response.setData(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Select Comment Error =>"+error);
        }
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

    //지금 생각으로는 완벽
    @ApiOperation(value = "댓글 총 개수 불러오기", notes = "글에 달린 댓글의 총 개수를 가져옴")
    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public ResponseEntity<?> getCommentTotal(@RequestParam long boardId){
        Response response = new Response();
        HttpStatus status;
        try {
            response = commentService.selectTotalById(boardId);
            status = response.getStatus();
        }catch (Exception e){
            String error = e.getMessage();
            response.setMessage("Fail to Request");
            response.setData(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Select Total Error =>"+error);
        }
        return new ResponseEntity<>(response, status);
    }
}
