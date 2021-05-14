package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.CommentCreateDTO;
import com.hang.myselfcommunity.dto.CommentDTO;
import com.hang.myselfcommunity.dto.ResultDTO;
import com.hang.myselfcommunity.enums.CommentTypeEnum;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.model.Comment;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
public class CommentController {

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {

        /* 没有登录不能回复 */
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            log.error("post method comment is empty");
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setLikeCount(0L);
        comment.setContent(commentCreateDTO.getContent());
        comment.setCommentCount(0);
        commentService.insert(comment);

        return ResultDTO.okOf();
    }

    @GetMapping("/comment/{id}")
    public ResultDTO<List> comments(@PathVariable Long id) {
        List<CommentDTO> commentDTOS = commentService.listByIdAndType(id, CommentTypeEnum.COMMENT);

        return ResultDTO.okOf(commentDTOS);
    }
}
