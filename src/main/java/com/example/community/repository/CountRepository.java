package com.example.community.repository;

import com.example.community.dto.Auth;
import com.example.community.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CountRepository {

    //select query
    Long selectCount(Long boardId);

    //insert query
    int firstCount(Long boardId);

    //update query
    int plusCount(Long boardId);

    //delete query

}
