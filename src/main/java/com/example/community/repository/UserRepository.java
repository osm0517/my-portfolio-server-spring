package com.example.community.repository;

import com.example.community.dto.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRepository{

    //select query
    List<User> selectAll();

    User selectPick(String email);

    String selectPassword(String email);

    //insert query
    int signup(User user);


    //update query
    void change(User user);


    //delete query
}
