package com.finshot.bulletin.controller;

import com.finshot.bulletin.dto.request.DeleteRequest;
import com.finshot.bulletin.entity.Post;
import com.finshot.bulletin.service.Interface.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private final PostService postService;
    private final PasswordEncoder passwordEncoder;

    public PostController(PostService postService, PasswordEncoder passwordEncoder) {
        this.postService = postService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/posts")
    public String getPosts(Model model) {
        List<Post> postList = postService.getAll();
        model.addAttribute("posts",postList);
        return "posts";
    }

    @GetMapping("posts/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        Post post = postService.getById(id);
        postService.viewPost(id);
        model.addAttribute("post", post);
        return "post-detail";
    }

    @GetMapping("posts/update/{id}")
    public String showUpdatePage(@PathVariable Long id, Model model) {
        Post post = postService.getById(id); // Ambil data postingan berdasarkan ID
        model.addAttribute("post", post);    // Kirim data postingan ke view
        return "update-post";               // Nama file HTML (update-post.html)
    }


    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create-post";
    }


    @PostMapping("/posts")
    public String createPost(@ModelAttribute("post") Post post) {
        postService.create(post);
        return "redirect:/posts";
    }

    @GetMapping("/")
    public String starter(){
        return "redirect:/posts";
    }


    @PostMapping("/posts/update")
    public String updatePost(@ModelAttribute Post post, @RequestParam String password) {
        Post existingPost = postService.getById(post.getId());
        if (!passwordEncoder.matches(password, existingPost.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password tidak cocok!");
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(new Date());
        postService.update(existingPost);

        return "redirect:/posts";
    }


    @PostMapping("/posts/delete")
    public String deletePost(@RequestParam("postId") Long postId,
                             @RequestParam("password") String password,
                             RedirectAttributes redirectAttributes) {

        DeleteRequest request = new DeleteRequest(postId,password);
        postService.delete(request);
        boolean isDeleted = true;
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Postingan berhasil dihapus.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Password salah. Postingan gagal dihapus.");
        }
        return "redirect:/posts";
    }

}
