package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.enums.NotificationTypeEnum;
import com.hang.myselfcommunity.model.Notification;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.service.CommentService;
import com.hang.myselfcommunity.service.NotificationService;
import com.hang.myselfcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private CommentService commentService;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request, @PathVariable Long id) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        Notification notification = notificationService.read(id, user);
        if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {
            return "redirect:/question/" + notification.getOuterId();
        } else if (notification.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()) {
            Long parentId = commentService.getParentId(notification.getOuterId());
            return "redirect:/question/" + parentId;
        } else {
            return "profile";
        }
    }

    @GetMapping("/readAll/{userId}")
    public String readAll(@PathVariable Long userId) {
        notificationService.readAll(userId);
        return "redirect:/profile/replies";
    }
}
