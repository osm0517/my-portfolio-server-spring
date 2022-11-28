package com.example.community.service;

import com.example.community.dto.Board;
import com.example.community.dto.Response;
import com.example.community.dto.Search;
import com.example.community.repository.BoardRepository;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BoardService {

    @Autowired
    BoardRepository repository;

    public Response write(Board board)
        throws Exception{
        Response response = new Response();

        //글의 내용이 존재하지 않으면 NullPointerException을 발생
        if(Objects.equals(board.getBoardText(), null)){ throw new NullPointerException("board text null!!!!"); }

        int boardResult = repository.writeBoard(board);

        if(boardResult != 1) {
            response.setMessage("board write error");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("OK");
            response.setStatus(HttpStatus.OK);
        }
        return response;
    }

    public Response detail(Long boardId)
            throws Exception{

        Response response = new Response();
        Board boardData = repository.readDetail(boardId);

        response.setData(boardData);
        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    // 검색을 할 때에 searchType은 총 4가지로 일단은 나눔
    // 작성자, 제목, 내용, 제목+내용
    // 일단은 제목 + 내용 하는 것은 뒤로 미루고 3가지만 먼저
    public Response search(Search search)
            throws Exception{

        Response result = new Response();

        System.out.println("BoardService Input = "+search);

        List<String> boardData = repository.searchBoard(search);

        if(Objects.equals(boardData, null)) throw new Error("Data Not Found");

        result.setData(boardData);
        result.setMessage("데이터가 정상적으로 불러옴");
        return result;
    }

    public Response updateByBoardId(Long boardId, Board board)
            throws Exception{

        Response result = new Response();
        BoardUpdateEntity entity = new BoardUpdateEntity(boardId, board);

        //데이터가 없으면 error를 반환
        if(Objects.equals(entity.getBoardText(), null) && Objects.equals(entity.getTitle(), null) &&
                Objects.equals(entity.getCategoryId(), null)) throw new Error("Data Not Found");

        if(Objects.equals(boardId, null)) throw new Error("BoardId Not Found");

        int updateResult = repository.updateByBoardId(entity);

        //업데이트를 할 때에 반환이 1아 아니면 제대로 안된 것
        if(updateResult != 1) {
            throw new Error("Post Not Exist");
        }

        result.setMessage("OK");
        result.setStatus(HttpStatus.OK);
        return result;
    }

    public Response findPostFromDB(int page, String sortType,
                                   int category, String searchQuery)
        throws Exception{
        //1페이지에 얼마나 보여줄지 결정
        int listTotal = 5;

        Response response = new Response();

        //offset 값을 결정
        int gap = (page - 1) * listTotal;
        PagingEntity entity = new PagingEntity(gap, listTotal, sortType, category, searchQuery);

        List<Board> result = repository.findPostFromDB(entity);

        response.setMessage("OK");
        response.setStatus(HttpStatus.OK);
        response.setData(result);

        return response;

    }

    public Response findTotalByAll()
            throws Exception{

        Response response = new Response();

        //총합을 가져옴
        int total = repository.findTotalByAll();

        response.setMessage("OK");
        response.setData(total);
        response.setStatus(HttpStatus.OK);

        return response;
    }

    public Response deleteByBoardId(Long boardId)
        throws Exception{

        Response response = new Response();

        int deleteResult = repository.deleteByBoardId(boardId);

        if(deleteResult != 1) throw new Error("Board Delete Error");

        response.setMessage("정상적으로 데이터가 삭제 되었습니다");
        response.setStatus(HttpStatus.OK);

        return response;
    }

}
