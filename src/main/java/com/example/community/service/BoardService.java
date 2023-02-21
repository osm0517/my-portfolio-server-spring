//package com.example.community.service;
//
//import com.example.community.model.DAO.board.Board;
//import com.example.community.dto.Response;
//import com.example.community.repository.board.BoardRepository;
//import com.example.community.vo.BoardUpdateEntity;
//import com.example.community.vo.PagingEntity;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class BoardService {
//
//   private final BoardRepository repository;
//
//    public Response write(Board board)
//        throws NullPointerException {
//        Response response = new Response();
//
//        //글의 내용이 존재하지 않으면 NullPointerException을 발생
//
//        int boardResult = repository.writeBoard(board);
//
//        if(boardResult != 1) {
//            response.setMessage("board write error");
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        }else{
//            response.setMessage("OK");
//            response.setStatus(HttpStatus.OK);
//        }
//        return response;
//    }
//
//    public Response detail(Long boardId){
//
//        Response response = new Response();
//        Board boardData = repository.readDetail(boardId);
//
//        response.setData(boardData);
//        response.setMessage("OK");
//        response.setStatus(HttpStatus.OK);
//        return response;
//    }
//
//    public Response updateByBoardId(Long boardId, Board board)
//            throws Error{
//
//        Response result = new Response();
//        BoardUpdateEntity entity = new BoardUpdateEntity(boardId, board);
//
//        //데이터가 없으면 error를 반환
//        if(Objects.equals(entity.getBoardText(), null) && Objects.equals(entity.getTitle(), null) &&
//                Objects.equals(entity.getCategoryId(), null)) throw new Error("Data Not Found");
//
//        if(Objects.equals(boardId, null)) throw new Error("BoardId Not Found");
//
//        int updateResult = repository.updateByBoardId(entity);
//
//        //업데이트를 할 때에 반환이 1아 아니면 제대로 안된 것
//        if(updateResult != 1) {
//            throw new Error("Post Not Exist");
//        }
//
//        result.setMessage("OK");
//        result.setStatus(HttpStatus.OK);
//        return result;
//    }
//
//    public Response findPostFromDB(int page, String sortType,
//                                   int category, String searchQuery){
//        //1페이지에 얼마나 보여줄지 결정
//        int listTotal = 5;
//
//        Response response = new Response();
//
//        //offset 값을 결정
//        int gap = (page - 1) * listTotal;
//        PagingEntity entity = new PagingEntity(gap, listTotal, sortType, category, searchQuery);
//
//        List<Board> result = repository.findPostFromDB(entity);
//
//        response.setMessage("OK");
//        response.setStatus(HttpStatus.OK);
//        response.setData(result);
//
//        return response;
//
//    }
//
//    public Response findTotalByAll(){
//
//        Response response = new Response();
//
//        //총합을 가져옴
//        int total = repository.findTotalByAll();
//
//        response.setMessage("OK");
//        response.setData(total);
//        response.setStatus(HttpStatus.OK);
//
//        return response;
//    }
//
//    public Response deleteByBoardId(Long boardId){
//
//        Response response = new Response();
//
//        int deleteResult = repository.deleteByBoardId(boardId);
//
//        response.setMessage("정상적으로 데이터가 삭제 되었습니다");
//        response.setStatus(HttpStatus.OK);
//
//        return response;
//    }
//
//}
