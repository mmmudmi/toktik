package com.scalable.toktik.controller;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.video.VideoDetailRecord;
import com.scalable.toktik.record.video.VideoRecordTool;
import com.scalable.toktik.service.UserService;
import com.scalable.toktik.service.VideoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/u")
@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {
    private final VideoService videoService;
    private final UserService userService;
    private final VideoRecordTool videoRecordTool;

    public UserController(VideoService videoService, UserService userService, VideoRecordTool videoRecordTool) {
        this.videoService = videoService;
        this.userService = userService;
        this.videoRecordTool = videoRecordTool;
    }

    @GetMapping("/profile")
    public List<VideoDetailRecord> userProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserModel user = userService.findByUsername(userDetails.getUsername());
        List<VideoModel> videos = videoService.findAllByUser(user);
        return videoRecordTool.createDetailRecordList(videos);
    }
}
