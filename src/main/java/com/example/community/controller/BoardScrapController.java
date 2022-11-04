package com.example.community.controller;

import com.example.community.dto.Response;
import com.example.community.dto.Scrap;
import com.example.community.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/scrap")
public class BoardScrapController {

    @Autowired
    ScrapService scrapService;

    //스크랩을 생성함
    @RequestMapping(value = "/{boardId}", method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable String boardId){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;
        try{
            Long board = Long.valueOf(boardId);
            //테스트용
            Long userId = (long) 1;

            result = scrapService.create(new Scrap(board, userId));
        }catch (Exception e){
            String error = e.toString();
            result.setMessage(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Scrap Creating Error =>"+error);
        }
        return new ResponseEntity<>(result, status);
    }

    //스크랩을 취소하고 삭제함
    @RequestMapping(value = "/{boardId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String boardId){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;

        try {
            Long board = Long.valueOf(boardId);
            //테스트용
            Long userId = (long) 1;

            result = scrapService.delete(new Scrap(board, userId));
        }catch (Exception e){
            String error = e.toString();
            result.setMessage(error);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println("Scrap Creating Error =>"+error);
        }
        return new ResponseEntity<>(result, status);
    }

    //게시글을 스크랩 한 횟수를 가져옴
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<?> count(@PathVariable String boardId){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;

        try {
            Long board = Long.valueOf(boardId);
            //테스트용
            Long userId = (long) 1;

            result = scrapService.count(new Scrap(board, userId));
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
