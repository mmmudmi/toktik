package com.scalable.toktik.controller;

import com.amazonaws.HttpMethod;
import com.scalable.toktik.model.CommentModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.comment.CommentForm;
import com.scalable.toktik.record.comment.CommentRecord;
import com.scalable.toktik.record.comment.CommentRecordTool;
import com.scalable.toktik.record.response.BoolResponse;
import com.scalable.toktik.record.response.ObjectResponse;
import com.scalable.toktik.record.s3.S3CompleteForm;
import com.scalable.toktik.record.video.VideoDetailRecord;
import com.scalable.toktik.record.video.VideoRecordTool;
import com.scalable.toktik.record.video.VideoSimpleRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.s3.AwsS3Service;
import com.scalable.toktik.service.*;
import com.scalable.toktik.websocket.UserSocketController;
import com.scalable.toktik.websocket.VideoSocketController;
import com.scalable.toktik.websocket.type.NotificationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/video")
public class VideoController {

    private final AwsS3Service awsS3Service;
    private final RedisService redisService;
    private final VideoService videoService;
    private final UserService userService;
    private final LikeService likeService;
    private final DislikeService dislikeService;
    private final CommentService commentService;
    private final VideoRecordTool videoRecordTool;
    private final VideoSocketController videoSocketController;
    private final UserSocketController userSocketController;

    @Value("${redis.queue.convert}")
    private String convertQueue;
    @Value("${redis.queue.preview}")
    private String previewQueue;
    @Value("${aws.bucketName}")
    private String bucketName;

    public VideoController(AwsS3Service awsS3Service, RedisService redisService, VideoService videoService, UserService userService, LikeService likeService, DislikeService dislikeService, CommentService commentService, VideoRecordTool videoRecordTool, VideoSocketController videoSocketController, UserSocketController userSocketController) {
        this.awsS3Service = awsS3Service;
        this.redisService = redisService;
        this.videoService = videoService;
        this.userService = userService;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
        this.commentService = commentService;
        this.videoRecordTool = videoRecordTool;
        this.videoSocketController = videoSocketController;
        this.userSocketController = userSocketController;
    }

    @GetMapping("/upload-url/{extension}")
    @PreAuthorize("isAuthenticated()")
    public BoolResponse generateUploadUrl(@PathVariable String extension) {
        if (extension.isBlank()) {
            return new BoolResponse(false, "Extension variable is require");
        }
        return new BoolResponse(true, awsS3Service.generateNewUploadUrl(extension, bucketName, 30));
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public BoolResponse uploadComplete(S3CompleteForm s3CompleteForm, @AuthenticationPrincipal UserDetails userDetails) {
        videoService.createVideo(s3CompleteForm.filename(), s3CompleteForm.caption(), userService.findByUsername(userDetails.getUsername()));
        redisService.sendMessageToQueue(previewQueue, s3CompleteForm.filename());
        redisService.sendMessageToQueue(convertQueue, s3CompleteForm.filename());
        return new BoolResponse(true, "Successfully recorded");
    }


    @GetMapping("/delete/{filename}")
    @PreAuthorize("isAuthenticated()")
    public BoolResponse deleteRequest(@PathVariable String filename, @AuthenticationPrincipal UserDetails userDetails) {
        VideoModel video = videoService.findByVideo(filename).orElse(null);
        if (video == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!video.getUser().getId().equals(userService.findByUsername(userDetails.getUsername()).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        awsS3Service.deletedObject(bucketName, video);
        return new BoolResponse(true, "Successfully deleted");
    }

    @GetMapping("/latest")
    @PreAuthorize("isAuthenticated()")
//    @Cacheable(value = "video", key = "{#methodName, #page, #size, #order}")
    public List<VideoSimpleRecord> getLatest(@RequestParam(defaultValue = "0", required = false) Integer page,
                                             @RequestParam(defaultValue = "20", required = false) Integer size,
                                             @RequestParam(defaultValue = "desc", required = false) String order) {
        boolean isDesc = order.startsWith("desc");
        return videoRecordTool.createVideoSimepleRecordList(videoService.getLatest(page, size, isDesc));
    }

    @GetMapping("/views")
    @PreAuthorize("isAuthenticated()")
    public List<VideoSimpleRecord> getMostView(@RequestParam(defaultValue = "0", required = false) Integer page,
                                               @RequestParam(defaultValue = "20", required = false) Integer size,
                                               @RequestParam(defaultValue = "desc", required = false) String order) {
        boolean isDesc = order.startsWith("desc");
        return videoRecordTool.createVideoSimepleRecordList(videoService.getByViews(page, size, isDesc));
    }

    @Cacheable(value = "VideoInfo", key = "{#filename}")
    @GetMapping("/playlist/{filename}")
    public @ResponseBody byte[] requestPresignPlaylist(@PathVariable String filename) {
        if (filename.endsWith("m3u8")) {
            String playliist = awsS3Service.downloadPlaylist(filename, bucketName);
            String withoutExt = filename.replace(".m3u8", "");
            StringBuilder content = new StringBuilder();
            for (String line : playliist.split("\n")) {
                if (line.startsWith(withoutExt) && line.endsWith(".ts")) {
                    line = awsS3Service.generatePreSignedUrl(HttpMethod.GET, withoutExt + "/" + line.strip(), bucketName, 30);
                }
                content.append(line).append("\n");
            }
            VideoModel video = videoService.findByVideo(filename).orElse(null);
            if (video == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            videoService.increaseView(video);
            videoSocketController.viewCountSocket(filename, video.getViews());
            return content.toString().getBytes();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/detail/{filename}")
    @PreAuthorize("isAuthenticated()")
    public VideoDetailRecord videoDetail(@PathVariable String filename, @AuthenticationPrincipal UserDetails userDetails) {
        VideoModel video = videoService.findByVideo(filename).orElse(null);
        if (video == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return videoRecordTool.createDetailRecord(video, userService.findByUsername(userDetails.getUsername()));
    }

    @GetMapping("/like/{filename}")
    @PreAuthorize("isAuthenticated()")
    public BoolResponse videoLike(@PathVariable String filename, @AuthenticationPrincipal UserDetails userDetails) {
        /** @return new like state */
        VideoModel video = videoService.findByVideo(filename).orElse(null);
        if (video == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (likeService.like(video, userService.findByUsername(userDetails.getUsername()))) {
            UserModel user = userService.findByUsername(userDetails.getUsername());
            if (!userDetails.getUsername().equals(video.getUser().getUsername())) {
                userSocketController.sendNotification(user, video, NotificationType.LIKE);
            }
            videoSocketController.likeCountSocket(video.getVideo(), likeService.likeCount(video));
            return new BoolResponse(true, "You like this video");
        }
        videoSocketController.likeCountSocket(video.getVideo(), likeService.likeCount(video));
        return new BoolResponse(false, "You unlike this video");
    }

    @GetMapping("/dislike/{filename}")
    @PreAuthorize("isAuthenticated()")
    public BoolResponse videoDislike(@PathVariable String filename, @AuthenticationPrincipal UserDetails userDetails) {
        /** @return new like state */
        VideoModel video = videoService.findByVideo(filename).orElse(null);
        if (video == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (dislikeService.dislike(video, userService.findByUsername(userDetails.getUsername()))) {
            videoSocketController.dislikeCountSocket(video.getVideo(), dislikeService.dislikeCount(video));
            return new BoolResponse(true, "You like this video");
        }
        videoSocketController.dislikeCountSocket(video.getVideo(), dislikeService.dislikeCount(video));
        return new BoolResponse(false, "You unlike this video");
    }


    @PostMapping("/comment")
    @PreAuthorize("isAuthenticated()")
    public ObjectResponse<CommentRecord> submitComment(CommentForm commentForm, @AuthenticationPrincipal UserDetails userDetails) {
        VideoModel video = videoService.findByVideo(commentForm.video()).orElse(null);
        UserModel user = userService.findByUsername(userDetails.getUsername());
        if (video == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (commentForm.comment().isBlank()) {
            return new ObjectResponse<>(false, "Comment can't empty", null);
        }
        CommentModel comment = commentService.createComment(user, video, commentForm.comment());
        CommentRecord record = CommentRecordTool.createCommentRecord(comment);
        if (!userDetails.getUsername().equals(video.getUser().getUsername())) {
            userSocketController.sendNotification(user, video, NotificationType.COMMENT);
        }
        videoSocketController.commentSocket(video.getVideo(), record);
        return new ObjectResponse<>(true, "Successfully created new comment", record);
    }
}
