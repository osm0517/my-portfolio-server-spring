package com.example.community.controller;

import com.example.community.dto.Board;
import com.example.community.dto.Response;
import com.example.community.dto.Search;
import com.example.community.repository.BoardRepository;
import com.example.community.service.BoardService;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.jdbc.Null;
import org.apache.tomcat.jni.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Api(tags = {"글 관련 API"})
public class BoardController {

    @Autowired
    BoardService boardService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String  test(@RequestParam String a){
        if(Objects.equals(a, "")){
            System.out.println("testValue is Null");
        }
        return a;

    }

    //test success
    @ApiOperation(value = "글 작성")
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public ResponseEntity<?> write(@RequestBody Board board){

        Response response = new Response();
        HttpStatus status;

        try{
            response = boardService.write(board);
            status = response.getStatus();
        } catch (NullPointerException e){
            response.setMessage(e.getMessage());
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e){
            System.out.println("==========");
            System.out.println("BoardController Write Method Catch Exception");
            System.out.println("Write Error => " + e);
            System.out.println("==========");

            response.setMessage("BoardController Write Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }catch (java.lang.Error e){
            response.setMessage(e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "게시물 읽기", notes = "게시물 목록 중에서 클릭하여" +
            "읽을 때 발생하는 API임")
    @RequestMapping(value = "/read/detail", method = RequestMethod.GET)
    public ResponseEntity<?> detail(@RequestParam Long boardId){

        HttpStatus status;
        Response response = new Response();

        try{
            response = boardService.detail(boardId);
            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("BoardController ReadDetail Method Catch Exception");
            System.out.println("ReadDetail Error => " + e);
            System.out.println("==========");

            response.setMessage("BoardController ReadDetail Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로 완벽
    @ApiOperation(value = "글 수정", notes = "글을 수정하는 API임" +
            "현재는 HTTPMethod를 PATCH로 하였으나, 공식 문서를 찾아보니" +
            "POST에 조금 더 가까운 것 같음")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestParam Long boardId, @RequestBody Board board){
        Response response = new Response();
        HttpStatus status;

        System.out.println("Update BoardId => " + boardId);
        try{
            response = boardService.updateByBoardId(boardId, board);
            status = response.getStatus();
        }catch (Exception e){
            //지금 계속해서 여기서 에러가 걸림
            //에러를 여기서 처리해주는 것이 아닌 핸들링을 해주고 싶은데
            //방법을 조금 고민을 해봐야할 듯함
            response.setMessage(e.toString());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }catch (java.lang.Error e){
            response.setMessage(e.getMessage());
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(response, status);
    }

    //페이징 할 때 생각을 해야할 것들
    //1. 한 페이지에 들어갈 양만큼 데이터로 보내줘야함
    //2. 현재 정렬을 어떤 식으로 진행하는지
    //3. 정렬을 바꿀 때마다 보내줘야하니 1, 2번을 반드시 해야함
    //일단 정렬 방식은 최신순, 오래된순, 조회수순, 스크랩순으로 가능
    //sortType Value => latest, old, count, scrap
    // 지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "글 리스트 불러오기", notes = "글 리스트를 불러오는 로직을 구현함." +
            "//정렬 방식 -> { 최신순, 오래된순, 조회수순, 스크랩순 }" +
            "//sortType -> { latest, old, count, scrap }" +
            "무조건 category 값은 보내줘야함 default 1")
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam int page, @RequestParam String sortType,
                                  @RequestParam int category, @RequestParam String searchQuery){

        Response response = new Response();
        HttpStatus status;

        try{
            response = boardService.findPostFromDB(page, sortType, category, searchQuery);

            //getData를 했을 때 데이터가 존재하지 않으면 page가 끝났다고 판단
            if(Objects.equals(response.getData(), null)){
                response.setMessage("Page End");
            }

            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("BoardController Read Method Catch Exception");
            System.out.println("Read Error => " + e);
            System.out.println("==========");

            response.setMessage("BoardController Read Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "게시물 총 개수 가져오기", notes = "현재 검색은 고려하지 않고" +
            "일반적으로 전체 게시물을 볼 때만을 고려해서 만듦")
    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public ResponseEntity<?> findTotalByAll(){

        Response response = new Response();
        HttpStatus status;

        try{
            response = boardService.findTotalByAll();

            //getData를 했을 때 데이터가 존재하지 않으면 page가 끝났다고 판단
            if(Objects.equals(response.getData(), 0)){
                response.setMessage("Page End");
            }

            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("BoardController findTotalByAll Method Catch Exception");
            System.out.println("findTotalByAll Error => " + e);
            System.out.println("==========");

            response.setMessage("BoardController findTotalByAll Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //지금 생각으로는 완벽하게 짬
    @ApiOperation(value = "글 삭제")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int boardId){
        Response response = new Response();
        HttpStatus status;

        Long id = (long) boardId;

        try{
            response = boardService.deleteByBoardId(id);
            status = response.getStatus();
        }catch (Exception e){
            System.out.println("==========");
            System.out.println("BoardController Delete Method Catch Exception");
            System.out.println("Delete Error => " + e);
            System.out.println("==========");

            response.setMessage("BoardController Delete Method Catch Exception");
            response.setData(e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }catch (java.lang.Error e){
            response.setMessage(e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
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
