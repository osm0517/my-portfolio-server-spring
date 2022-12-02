package com.example.community.controller;

import com.example.community.dto.Response;
import com.example.community.dto.Scrap;
import com.example.community.service.ScrapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@Api(tags = {"scrap 관련한 API"})
@RequestMapping("/scrap")
public class BoardScrapController {

    @Autowired
    ScrapService scrapService;

    //스크랩을 생성함
    //지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "스크랩 생성", notes = "스크랩을 추가함" +
            "//스크랩 횟수를 1회 올리고, 스크랩 목록에 추가함")
    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable String boardId, HttpServletRequest request){
        Response result = new Response();
        HttpStatus status;
        try{
            Long board = Long.valueOf(boardId);

            result = scrapService.create(board, request);
            status = result.getStatus();
        }catch (Exception e){
            String error = e.toString();
            result.setMessage(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Scrap Creating Error =>"+error);
        }
        return new ResponseEntity<>(result, status);
    }

    //스크랩을 취소하고 삭제함
    //지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "스크랩 취소", notes = "스크랩을 취소함" +
            "//스크랩 목록에서는 삭제 //스크랩 횟수도 1회 낮아져야 함")
    @RequestMapping(value = "/{boardId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String boardId, HttpServletRequest request){
        Response result = new Response();
        HttpStatus status;

        try {
            long board = Long.parseLong(boardId);

            result = scrapService.delete(board, request);
            status = result.getStatus();
        }catch (Exception e){
            String error = e.toString();
            result.setMessage(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Scrap Creating Error =>"+error);
        }
        return new ResponseEntity<>(result, status);
    }

    //게시글을 스크랩 한 횟수를 가져옴
    //scrap을 한 횟수를 나타내는 테이블을 생성하고 수정하는 걸로 함
    @ApiOperation(value = "스크랩 횟수를 조회", notes = "글 기준으로 스크랩" +
            "횟수를 조회함")
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<?> count(@PathVariable String boardId){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;

        try {
            Long board = Long.valueOf(boardId);
            //테스트용
            Long userId = (long) 1;

//            result = scrapService.count(new Scrap(board, userId));
        }catch (Exception e){
            String error = e.toString();
            result.setMessage(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Scrap Counting Error =>"+error);
        }
        return new ResponseEntity<>(result, status);
    }

    @ExceptionHandler(value = Error.class)
    public void errorHandler(Error error){
        System.out.println(error.toString());
    }
}
