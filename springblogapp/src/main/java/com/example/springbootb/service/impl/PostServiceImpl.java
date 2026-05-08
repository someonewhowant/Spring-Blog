package com.example.springbootb.service.impl;

import com.example.springbootb.dto.PostDto;
import com.example.springbootb.entity.Post;
import com.example.springbootb.entity.User;
import com.example.springbootb.mapper.PostMapper;
import com.example.springbootb.repository.PostRepository;
import com.example.springbootb.repository.UserRepository;
import com.example.springbootb.service.PostService;
import com.example.springbootb.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PostDto> findPostByUser() {
        org.springframework.security.core.userdetails.User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        String email = currentUser.getUsername();
        User createdBy = userRepository.findByEmail(email);
        if (createdBy == null) {
            return List.of();
        }
        Long userId = createdBy.getId();
        List<Post> posts = postRepository.findPostsByUser(userId);
        return posts.stream().map(PostMapper::mapToPostDto).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map((PostMapper::mapToPostDto)).collect(Collectors.toList());
    }

    @Override
    public void createPost(PostDto postDto) {
        org.springframework.security.core.userdetails.User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("No authenticated user found");
        }
        String email = currentUser.getUsername();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Current user not found in the database");
        }

        // Check if URL is already taken and append a count if needed
        String url = postDto.getUrl();
        if (postRepository.findByUrl(url).isPresent()) {
            url = url + "-" + System.currentTimeMillis();
        }

        Post post = PostMapper.mapToPost(postDto);
        post.setId(null); // Ensure it's a new post
        post.setUrl(url);
        post.setCreatedBy(user);
        postRepository.save(post);
    }

    @Override
    public PostDto findPostById(Long postId) {
        Post post = postRepository.findById(postId).get();
        return PostMapper.mapToPostDto(post);
    }

    @Override
    public void updatePost(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId()).get();
        post.setTitle(postDto.getTitle());
        post.setShortDescription(postDto.getShortDescription());
        post.setContent(postDto.getContent());
        post.setUrl(postDto.getUrl());
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public PostDto findPostByUrl(String postUrl) {
        Post post = postRepository.findByUrl(postUrl).get();
        return PostMapper.mapToPostDto(post);
    }

    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream().map(PostMapper::mapToPostDto).collect(Collectors.toList());
    }
}
