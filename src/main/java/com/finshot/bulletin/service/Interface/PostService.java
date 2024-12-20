package com.finshot.bulletin.service.Interface;

import com.finshot.bulletin.dto.request.DeleteRequest;
import com.finshot.bulletin.entity.Post;

import java.util.List;

public interface PostService {
    Post create(Post request);

    List<Post> getAll();
    Post getById(Long id);
    Post update(Post request);
    void delete(DeleteRequest request);
    void viewPost(Long id);

}
