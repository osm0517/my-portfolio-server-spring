package com.example.community.vo;


import com.example.community.dto.Board;
import lombok.Data;
import lombok.Getter;
import lombok.Value;


@Getter
public class BoardUpdateEntity {

    private Long boardId;

    private String title;

    private Integer categoryId;

    private String boardText;

    //vo여서 setter가 없어야함
    //생성자를 통해서 모든 값을 다 넣어주고
    //getter로 값만 가져오는 것으로 만들기
    public BoardUpdateEntity(Long boardId, Board board) {
        this.boardId = boardId;

        this.boardText = board.getBoardText();

        this.title = board.getTitle();

        this.categoryId = board.getCategoryId();
    }




}
