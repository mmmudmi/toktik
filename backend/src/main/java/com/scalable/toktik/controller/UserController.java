package com.scalable.toktik.controller;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.notification.NotificationRecord;
import com.scalable.toktik.record.notification.NotificationRecordTool;
import com.scalable.toktik.record.response.BoolResponse;
import com.scalable.toktik.record.video.VideoRecordTool;
import com.scalable.toktik.record.video.VideoUserSimpleRecord;
import com.scalable.toktik.service.NotificationService;
import com.scalable.toktik.service.UserService;
import com.scalable.toktik.service.VideoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/u")
@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {
    private final VideoService videoService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final VideoRecordTool videoRecordTool;
    private final NotificationRecordTool notificationRecordTool;

    public UserController(VideoService videoService, UserService userService, NotificationService notificationService, VideoRecordTool videoRecordTool, NotificationRecordTool notificationRecordTool) {
        this.videoService = videoService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.videoRecordTool = videoRecordTool;
        this.notificationRecordTool = notificationRecordTool;
    }

    @GetMapping("/profile")
    public List<VideoUserSimpleRecord> userProfile(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                   @RequestParam(defaultValue = "20", required = false) Integer size,
                                                   @RequestParam(defaultValue = "desc", required = false) String order,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        UserModel user = userService.findByUsername(userDetails.getUsername());
        boolean isDesc = order.startsWith("desc");
        List<VideoModel> videos = videoService.findAllByUser(user, page, size, isDesc);
        return videoRecordTool.createVideoUserSimepleRecordList(videos);
    }

    @GetMapping("/notification")
    public List<NotificationRecord> userNotification(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                     @RequestParam(defaultValue = "10", required = false) Integer size,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        UserModel user = userService.findByUsername(userDetails.getUsername());
        return notificationRecordTool.createNotificationList(notificationService.latestNotification(user, page, size));
    }

    @PostMapping("/notification/read")
    public BoolResponse readNotification(@AuthenticationPrincipal UserDetails userDetails) {
        UserModel user = userService.findByUsername(userDetails.getUsername());
        notificationService.readAll(user);
        return new BoolResponse(true, "All notifications was read");
    }
}
