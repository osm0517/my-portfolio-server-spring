package com.example.community.repository;

import com.example.community.dto.Auth;
import com.example.community.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRepository{

    //select query
    List<User> selectAll();

    String selectPassword(String userId);

    String findAuthByUserId(String userId);

    int countAuth(String email);

    String selectAuth(String email);

    String infoOverlap(User user);

    //insert query
    int signup(User user);

    void createAuth(Auth data);



    //update query
    void change(User user);


    //delete query
    void delete(String email);

    int deleteAuth(String email);
}
