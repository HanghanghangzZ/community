package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.exception.CustomizeException;
import com.hang.myselfcommunity.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    public static void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
    }
}
