package com.example.community.service;

import com.example.community.repository.board.CommentSubRepository;
import com.example.community.repository.report.CommentSubReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentSubService {

    private final CommentSubRepository commentSubRepository;
    private final CommentSubReportRepository commentSubReportRepository;

    public void writeCommentSub(){

    }

    public void CommentSubs(){

    }

    public void deleteCommentSub(){

    }

    public void reportCommentSub(){

    }

}
