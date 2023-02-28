package com.example.community.service;

import com.example.community.data.category.Category;
import com.example.community.model.DAO.board.Post;
import com.example.community.model.DAO.user.User;
import com.example.community.model.DTO.post.PostEditDTO;
import com.example.community.model.DTO.post.PostWriteDTO;
import com.example.community.repository.board.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<Post> posts(User user, Pageable request) {
        if(request == null || user == null){
            throw new NullPointerException("request can not be null");
        }
        return postRepository.findAllByUserIdNot(user.getId(), request)
                .stream().toList();
    }

    @Transactional
    public Post writePost(PostWriteDTO postWriteDTO) throws IllegalArgumentException {
        try{
            if(postWriteDTO.hasNull()){
                throw new IllegalArgumentException("post variable can not be null");
            }else{
                String title = postWriteDTO.getTitle();
                String text = postWriteDTO.getText();
                User user = postWriteDTO.getUser();
                Category category = postWriteDTO.getCategory();
                Category detailCategory = postWriteDTO.getDetailCategory();

                Post post = new Post(title, text, user, category, detailCategory);

                return postRepository.save(post);
            }
        }catch (IllegalAccessException e){
            log.error(e.getMessage());
            log.error("postWriteDTO checkNull method access level error");
            return null;
        }
    }

    @Transactional
    public void editPost(long postId, PostEditDTO postEditDTO){
        try{
            if(postEditDTO.hasNull()){
                throw new IllegalArgumentException("post variable can not be null");
            }else{
                Post post = postRepository.findById(postId)
                        .orElseThrow(NoSuchElementException::new);

                Post editPost = post.editPost(postEditDTO);

                postRepository.save(editPost);
            }
        }catch (IllegalAccessException e){
            log.error(e.getMessage());
            log.error("postEditDTO method access level error");
        }
    }

    @Transactional
    public Post detail(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(NoSuchElementException::new);
    }
}
