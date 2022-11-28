package com.example.community.repository;

import com.example.community.dto.Board;
import com.example.community.dto.Search;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardRepository {

    //페이징을 할 때에 사용하는 메소드
    //게시물의 총 개수를 가져옴
    //현재는 검색을 사용하지 않았을 때만 고려
    int findTotalByAll();

    int writeBoard(Board board);

    Board readDetail(Long boardId);

    List<String> searchBoard(Search search);

    //list를 가져와서 페이징하는 sql
    List<Board> findPostFromDB(PagingEntity entity);

    int updateByBoardId(BoardUpdateEntity entity);

    int deleteByBoardId(Long boardId);
}
