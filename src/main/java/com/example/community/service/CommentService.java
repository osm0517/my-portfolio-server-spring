package com.example.community.service;

import com.example.community.dto.Comment;
import com.example.community.dto.Report;
import com.example.community.dto.Response;
import com.example.community.repository.CommentRepository;
import com.example.community.utils.jwt.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
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

    private String defaultComment = "정말 좋아요:)";

//    select
    public Response selectCommentById(long boardId){

        Response response = new Response();
        //저장된 comment를 불러옴
        List<Comment> commentList = repository.selectCommentById(boardId);

        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        response.setData(commentList);

        return response;
    }
    public Response selectTotalById(long boardId){

        Response response = new Response();
        //해당 boardId에 댓글의 총 개수를 불러옴
        int selectResult = repository.commentTotal(boardId);

        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        response.setData(selectResult);

        return response;
    }
//    insert
    public Response createComment(Comment comment){

        Response response = new Response();
        //댓글 내용이 없다면 default 값을 넣어서 댓글을 작성함.
        String text = comment.getComment();
        if(Objects.equals(text, null)){ comment.setComment(defaultComment); }
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

//    public Response addComment(Comment comment)
//            throws Exception{
//        Response response = new Response();
//        HttpStatus status;
//        //댓글 내용이 없다면 default 값을 넣어서 댓글을 작성함.
//        String text = comment.getComment();
//        if(Objects.equals(text, null)){ comment.setComment(defaultComment); }
//        //대댓글을 작성함
//        int createResult = repository.addComment(comment);
//        //제대로 만들어지지 않으면 예외를 반환
//        //일단은 badRequest를 반환
//        if(createResult != 1) {
//            response.setMessage("Fail To Create");
//            response.setStatus(HttpStatus.BAD_REQUEST);
//        }
//        response.setMessage("OK");
//        response.setStatus(HttpStatus.OK);
//
//        return response;
//    }

    public Response reportComment(Report report){

        Response response = new Response();
        HttpStatus status;
        //댓글 신고를 작성함
        int createResult = repository.reportComment(report);
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
    public Response deleteComment(long boardId, int commentId, HttpServletRequest request){

        Response response = new Response();
        //header에서 access쿠키 값을 가져옴
        String token = jwtConfig.resolveToken(request);
        //해당 값에서 userId값을 가져옴
        String userId = cookieService.searchUserIdByCookie(token, accessSecretKey);

        //요청한 boardId에 해당 commentId가 존재하는지 확인
        List<Integer> selectIdResult = repository.selectIdByBoardId(boardId);

        if(selectIdResult.contains(commentId)){
            //일반 사용자인지 관리자인지를 구분
            //관리자일 때
            if( userId.contains(checkAdminString) ){
                int deleteResult = repository.deleteComment(commentId);

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
                int createResult = repository.deleteComment(commentId);
                //제대로 삭제되지 않으면 예외를 반환
                //일단은 badRequest를 반환
                if(createResult != 1) {
                    response.setMessage("Fail To Create");
                    response.setStatus(HttpStatus.BAD_REQUEST);
                }
                response.setMessage("OK");
                response.setStatus(HttpStatus.OK);
            }
        }else{
            response.setMessage("CommentId Not Exist To Posts");
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
