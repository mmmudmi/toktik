package com.scalable.toktik.record.comment;

import com.scalable.toktik.model.CommentModel;

import java.util.List;
import java.util.Set;

public class CommentRecordTool {
    public static CommentRecord createCommentRecord(CommentModel comment) {
        return new CommentRecord(comment.getUser().getUsername(), comment.getComment(), comment.getCreated());
    }

    public static List<CommentRecord> createcommentRecordList(Set<CommentModel> comments) {
        return comments.stream().map(CommentRecordTool::createCommentRecord).toList();
    }
}
