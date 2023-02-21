package com.example.community.repository.board;

import com.example.community.model.DAO.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
