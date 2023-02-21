package com.example.community.model.DAO.board;

import com.example.community.model.DAO.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "scrap")
public class Scrap {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //    일단 보류
//    private User user;

    //    일단 보류
//    private Board board;
}
