package com.scalable.toktik.record.comment;

import java.time.LocalDateTime;

public record CommentRecord(String username, String comment, LocalDateTime created) {
}
