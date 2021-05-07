package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.CommentDTO;
import com.hang.myselfcommunity.dto.ResultDTO;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.mapper.CommentMapper;
import com.hang.myselfcommunity.model.Comment;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {

        /* 没有登录不能回复 */
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        Comment comment = new Comment();
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setLikeCount(0L);
        comment.setContent(commentDTO.getContent());
        comment.setCommentCount(0);
        commentService.insert(comment);

        return ResultDTO.okOf();
    }
}
