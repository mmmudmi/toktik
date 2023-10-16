package com.scalable.toktik.redis;

import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.service.VideoService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {
     private final VideoService videoService;

    public RedisSubscriber(VideoService videoService) {
        this.videoService = videoService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Handle the incoming message
        String messageContent = new String(message.getBody());
        System.out.println(messageContent);
        // Add your processing logic here
        VideoModel video = videoService.findVideoStartWith(messageContent);
        video.setVideo(messageContent + ".m3u8");
        video.setPreview(messageContent + ".jpg");
        video.setIs_process(true);
        videoService.updateVideo(video);
    }
}
