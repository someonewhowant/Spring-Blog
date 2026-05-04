package com.example.blog.controller;

import com.example.blog.entity.Post;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    /**
     * Главная страница блога с пагинацией.
     * Аналог router.get('') из Node.js.
     */
    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<Post> postPage = postService.getAllPosts(page);
        
        model.addAttribute("data", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("title", "NodeJs Blog");
        model.addAttribute("description", "Simple Blog created with NodeJs, Express & MongoDB (Migrated to Spring Boot)");
        
        return "index";
    }

    /**
     * Просмотр конкретного поста по ID.
     * Аналог router.get('/post/:id') из Node.js.
     */
    @GetMapping("/post/{id}")
    public String post(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        
        model.addAttribute("post", post);
        model.addAttribute("title", post.getTitle());
        
        return "post";
    }

    /**
     * Поиск постов по ключевому слову.
     * Аналог router.post('/search') или логики поиска из Node.js.
     */
    @GetMapping("/search")
    public String search(@RequestParam String searchTerm, 
                         @RequestParam(defaultValue = "1") int page, 
                         Model model) {
        Page<Post> searchResults = postService.searchPosts(searchTerm, page);
        
        model.addAttribute("data", searchResults.getContent());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("title", "Search Results");
        
        return "search";
    }

    /**
     * Статическая страница "About".
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us");
        return "about";
    }
}
