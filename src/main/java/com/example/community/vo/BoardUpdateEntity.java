package com.example.community.vo;


import com.example.community.model.DAO.board.Post;
import lombok.Getter;


@Getter
public class BoardUpdateEntity {

    private Long boardId;

    private String title;

    private Integer categoryId;

    private String boardText;

    //vo여서 setter가 없어야함
    //생성자를 통해서 모든 값을 다 넣어주고
    //getter로 값만 가져오는 것으로 만들기
    public BoardUpdateEntity(Long boardId, Post post) {
        this.boardId = boardId;

    }




}
