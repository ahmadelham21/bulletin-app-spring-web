package com.finshot.bulletin.service;

import com.finshot.bulletin.dto.request.DeleteRequest;
import com.finshot.bulletin.entity.Post;
import com.finshot.bulletin.repository.PostRepository;
import com.finshot.bulletin.service.Interface.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service

public class PostServiceImplement implements PostService {

    @Autowired
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public PostServiceImplement(PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Post create(Post request) {
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        Post newPost = new Post();
        newPost.setTitle(request.getTitle());
        newPost.setAuthor(request.getAuthor());
        newPost.setContent(request.getContent());
        newPost.setPassword(encryptedPassword);
        newPost.setDeleted(false);
        newPost.setCreatedAt(new Date());
        newPost.setUpdatedAt(new Date());
        newPost.setViews(0);

        return postRepository.saveAndFlush(newPost);
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findByIsDeletedFalse();

    }

    @Override
    public Post getById(Long id) {
        return postRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Post not found"));

    }

    @Override
    public Post update(Post request) {
        return postRepository.saveAndFlush(request);
    }

    @Override
    public void delete(DeleteRequest request) {
        Post post = getById(request.getId());
        if (!passwordEncoder.matches(request.getPassword(), post.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password tidak cocok!");
        }
        post.setDeleted(true);
        postRepository.saveAndFlush(post);


    }

    @Override
    public void viewPost(Long id) {
       Post post = getById(id);
       post.setViews(post.getViews() + 1);
       postRepository.saveAndFlush(post);
    }


}
