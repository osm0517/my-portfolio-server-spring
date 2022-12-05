package com.example.community.service;

import com.example.community.dto.Comment;
import com.example.community.dto.Response;
import com.example.community.repository.CommentRepository;
import com.example.community.utils.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository repository;

    @Autowired
    CookieService cookieService;

    @Autowired
    JwtConfig jwtConfig;

    @Value("${jwt.access-secretKey}")
    private String accessSecretKey;

    private String checkAdminString = "_ADMIN";

//    select
//    public Response selectCommentById(long boardId)
//        throws Exception{
//        Response response = new Response();
//        HttpStatus status;
//        //저장된 comment를 불러옴
//        List<Comment> commentList = repository.selectCommentById(boardId);
//
//    }
//    insert
    public Response createComment(Comment comment)
        throws Exception{
        Response response = new Response();
        HttpStatus status;
        //댓글을 작성함
        int createResult = repository.createComment(comment);
        //제대로 만들어지지 않으면 예외를 반환
        //일단은 badRequest를 반환
        if(createResult != 1) {
            response.setMessage("Fail To Create");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public Response addComment(Comment comment)
            throws Exception{
        Response response = new Response();
        HttpStatus status;
        //대댓글을 작성함
        int createResult = repository.addComment(comment);
        //제대로 만들어지지 않으면 예외를 반환
        //일단은 badRequest를 반환
        if(createResult != 1) {
            response.setMessage("Fail To Create");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public Response reportComment(Comment comment)
            throws Exception{
        Response response = new Response();
        HttpStatus status;
        //댓글 신고를 작성함
        int createResult = repository.reportComment(comment);
        //제대로 만들어지지 않으면 예외를 반환
        //일단은 badRequest를 반환
        if(createResult != 1) {
            response.setMessage("Fail To Create");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        return response;
    }
//    update

//    delete
    public Response deleteComment(Comment comment, HttpServletRequest request)
            throws Exception{
        Response response = new Response();
        HttpStatus status;
        //header에서 access쿠키 값을 가져옴
        String token = jwtConfig.resolveToken(request);
        //해당 값에서 userId값을 가져옴
        String userId = cookieService.searchUserIdByCookie(token, accessSecretKey);

        //일반 사용자인지 관리자인지를 구분
        //관리자일 때
        if( userId.contains(checkAdminString) ){
            int deleteResult = repository.deleteUserComment(comment);

            //제대로 삭제되지 않으면 예외를 반환
            //일단은 badRequest를 반환
            if(deleteResult != 1) {
                response.setMessage("Fail To Delete");
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
            response.setMessage("OK");
            response.setStatus(HttpStatus.OK);
        }//아닐때
        else{
            //내 댓글을 삭제함
            int createResult = repository.deleteMyComment(comment);
            //제대로 삭제되지 않으면 예외를 반환
            //일단은 badRequest를 반환
            if(createResult != 1) {
                response.setMessage("Fail To Create");
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
            response.setMessage("OK");
            response.setStatus(HttpStatus.OK);
        }
        return response;
    }
}
