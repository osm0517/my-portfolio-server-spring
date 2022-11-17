package com.example.community.service;

import com.example.community.dto.Board;
import com.example.community.dto.Response;
import com.example.community.dto.Search;
import com.example.community.repository.BoardRepository;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BoardService {

    @Autowired
    BoardRepository repository;

    public String write(Board board)
        throws Exception{

        int boardResult = repository.writeBoard(board);

        if(boardResult > 0) return "db에 정상적으로 저장됨";
        else return "db에 정상적으로 저장안됨";

    }

    public Response detail(Long boardId)
            throws Exception{

        Response result = new Response();
        Board boardData = repository.readDetail(boardId);

        if(Objects.equals(boardData, null)) throw new Error("boardId not found");

        result.setData(boardData);
        result.setMessage("데이터가 정상적으로 불러옴");
        return result;
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

    public Response update(Long boardId, Board board)
            throws Exception{

        Response result = new Response();
        BoardUpdateEntity entity = new BoardUpdateEntity(boardId, board);

        //데이터가 없으면 error를 반환
        if(Objects.equals(entity.getBoardText(), null) && Objects.equals(entity.getTitle(), null)) throw new Error("Data Not Found");
        if(Objects.equals(boardId, null)) throw new Error("BoardId Not Found");

        int updateResult = repository.updateBoard(entity);

        //업데이트를 할 때에 반환이 1아 아니면 제대로 안된 것
        //제대로 안되면 error가 발생하는지 확인해봐야함
        if(updateResult != 1) throw new Error("Update Error");

        result.setMessage("데이터를 정상적으로 업데이트함");
        return result;
    }

    public Response read(int page, String sortType)
        throws Exception{
        //1페이지에 얼마나 보여줄지 결정
        int range = 4;

        Response response = new Response();

        int gap = page * range;
        PagingEntity entity = new PagingEntity(gap, range, sortType);

        List<Board> result = repository.boardPaging(entity);

        if(Objects.equals(result, null)) throw new Error("Paging End");

        response.setMessage("정상적으로 불러옴");
        response.setData(result);

        return response;

    }

    public Response delete(Long boardId)
        throws Exception{

        Response result = new Response();

        int deleteResult = repository.delete(boardId);

        if(deleteResult != 1) throw new Error("Board Delete Error");

        result.setMessage("정상적으로 데이터가 삭제 되었습니다");
        return result;
    }

}
