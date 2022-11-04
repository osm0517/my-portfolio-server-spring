package com.example.community.repository;

import com.example.community.dto.Board;
import com.example.community.dto.Scrap;
import com.example.community.dto.Search;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ScrapRepository {

    //일단 만듦

    //중복된 userId가 생기더라도 테이블에 계속 데이터를 쌓는 식으로
    //정리하자면 2가지 방식을 다 해본 것
    //나중에는 결정할 것
    int create(Scrap scrap);

    int delete(Scrap scrap);

    String count(Scrap scrap);

    //스크랩이 있는지 확인하기 위해서 check하는 용도
    //현재는 userId와 boardId 둘 다 맞을 때 있다고 판단
    //쿼리문에서 검증을 끝냄
    int scrapCheck(Scrap scrap);

    //글이 작성되면 가장 처음 스크랩 데이터를 넣기 위함
    int firstScrap(Scrap scrap);
}
