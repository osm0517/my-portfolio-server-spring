package com.example.community.service;

import com.example.community.dto.Board;
import com.example.community.dto.Response;
import com.example.community.dto.Scrap;
import com.example.community.dto.Search;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.ScrapRepository;
import com.example.community.vo.BoardUpdateEntity;
import com.example.community.vo.PagingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ScrapService {

    @Autowired
    ScrapRepository repository;

    public Response create(Scrap scrap)
        throws Exception{

        Response result = new Response();

        int createResult = repository.create(scrap);

        if(createResult != 1) throw new Error("Scrap Create Error");

        result.setMessage("스크랩이 정상적으로 추가됨");

        return result;
    }

    public Response delete(Scrap scrap)
            throws Exception{

        Response result = new Response();

        int deleteResult = repository.delete(scrap);

        if(deleteResult != 1) throw new Error("Scrap Delete Error");

        result.setMessage("스크랩이 정상적으로 삭제됨");

        return result;
    }

    public Response count(Scrap scrap)
            throws Exception{

        Response result = new Response();
        long value;

        String countResult = repository.count(scrap);

        if(Objects.equals(countResult, null)) throw new Error("Scrap Delete Error");

        value = Long.parseLong(repository.count(scrap));
        result.setMessage("스크랩 개수를 정상적으로 조회함");
        result.setData(value);

        return result;
    }
}
