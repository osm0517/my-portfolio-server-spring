package com.example.community.controller;

import com.example.community.dto.Board;
import com.example.community.dto.Response;
import com.example.community.dto.Search;
import com.example.community.repository.BoardRepository;
import com.example.community.service.BoardService;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @RequestMapping("/test")
    public void test(){
        System.out.println("TestController sout");

    }

    //test success
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public ResponseEntity<?> write(@RequestBody Board board){

        Response response = new Response();
        HttpStatus status = HttpStatus.OK;

        try{
            response.setMessage(boardService.write(board));

        }catch (Exception e){
            System.out.println(e);
        }
        return new ResponseEntity<>(response, status);
    }

    //test success
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> write(@RequestBody Search search){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;
        try{
            result = boardService.search(search);
        }catch (Exception e){
            result.setMessage(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    //test success(게시글을 클릭하여 게시글을 불러오는 동작)
    //페이징이랑 연결할 생각을 해봐야함
    @RequestMapping(value = "/read/detail", method = RequestMethod.GET)
    public ResponseEntity<?> write(@RequestParam Long boardId){

        HttpStatus status = HttpStatus.OK;
        Response response = new Response();

        try{
            response = boardService.detail(boardId);
        }catch (Exception e){
            System.out.println(e);
            response.setMessage(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //test success
    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<?> write(@RequestParam Long boardId, @RequestBody Board board){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;

        System.out.println("BoardId => " + boardId);
        try{
            result = boardService.update(boardId, board);
        }catch (Exception e){
            //지금 계속해서 여기서 에러가 걸림
            //에러를 여기서 처리해주는 것이 아닌 핸들링을 해주고 싶은데
            //방법을 조금 고민을 해봐야할 듯함
            result.setMessage(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    //페이징 할 때 생각을 해야할 것들
    //1. 한 페이지에 들어갈 양만큼 데이터로 보내줘야함
    //2. 현재 정렬을 어떤 식으로 진행하는지
    //3. 정렬을 바꿀 때마다 보내줘야하니 1, 2번을 반드시 해야함
    //일단 정렬 방식은 최신순, 오래된순, 조회수순, 스크랩순으로 가능
    //sortType Value => latest, old, count, scrap
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ResponseEntity<?> write(@RequestParam int page, @RequestParam String sortType){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;
        try{
            result = boardService.read(page, sortType);
        }catch (Exception e){
            result.setMessage(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    //test success
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int boardId){
        Response result = new Response();
        HttpStatus status = HttpStatus.OK;

        Long id = (long) boardId;

        try{
            result = boardService.delete(id);
        }catch (Exception e){
            result.setMessage(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    // error handler
    @ExceptionHandler(Error.class)
    public ResponseEntity<?> customErrorHandler(Error error){
        return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //정상적으로 exception을 처리하지 못함
    //위에 error class는 처리하는데 처리하지 못하는 이유를 확인할 것
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> customDataErrorHandler(DataIntegrityViolationException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
