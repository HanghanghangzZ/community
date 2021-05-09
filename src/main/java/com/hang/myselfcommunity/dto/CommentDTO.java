package com.hang.myselfcommunity.dto;

import com.hang.myselfcommunity.model.Comment;
import com.hang.myselfcommunity.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Comment comment;
    private User user;

    public CommentDTO(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}
