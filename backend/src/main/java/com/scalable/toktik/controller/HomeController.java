package com.scalable.toktik.controller;

import com.scalable.toktik.service.CommentService;
import com.scalable.toktik.service.UserService;
import com.scalable.toktik.service.VideoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
public class HomeController {
    private final VideoService videoService;
    private final UserService userService;
    private final CommentService commentService;

    public HomeController(VideoService videoService, UserService userService, CommentService commentService) {
        this.videoService = videoService;
        this.userService = userService;
        this.commentService = commentService;
    }

}