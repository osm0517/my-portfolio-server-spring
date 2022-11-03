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

    int writeBoard(Board board);

    Board readDetail(Long boardId);

    List<String> searchBoard(Search search);

    List<Board> boardPaging(PagingEntity entity);

    int updateBoard(BoardUpdateEntity entity);

    int delete(Long boardId);
}
