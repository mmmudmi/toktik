package com.scalable.toktik.record.video;

import com.scalable.toktik.record.comment.CommentRecord;

import java.time.LocalDateTime;
import java.util.List;

public record VideoDetailRecord(Long id, String video, String preview, String caption, Integer views, String username,
                                Integer like_count, Integer dislike_count, Integer comment_count, Boolean is_like,
                                LocalDateTime created, List<CommentRecord> comments) {
}
