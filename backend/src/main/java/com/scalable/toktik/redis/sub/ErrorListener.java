package com.scalable.toktik.redis.sub;

import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.service.VideoService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class ErrorListener implements MessageListener {
    private final VideoService videoService;

    public ErrorListener(VideoService videoService) {
        this.videoService = videoService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Handle the incoming message
        String messageContent = new String(message.getBody());
        System.out.println(messageContent);
        // Add your processing logic here
        VideoModel video = videoService.findVideoStartWith(messageContent).orElse(null);
        if (video == null) {
            return;
        }
        video.setStatus(-1);
        videoService.updateVideo(video);
    }
}