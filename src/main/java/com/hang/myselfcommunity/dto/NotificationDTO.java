package com.hang.myselfcommunity.dto;

import com.hang.myselfcommunity.model.Notification;
import com.hang.myselfcommunity.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Notification notification;
    private User notifier;
    /* 所通知的问题或评论名 */
    private String outerTitle;
    private String typeReplyString;

    public NotificationDTO() {
    }

    public NotificationDTO(Notification notification, User notifier, String outerTitle, String typeReplyString) {
        this.notification = notification;
        this.notifier = notifier;
        this.outerTitle = outerTitle;
        this.typeReplyString = typeReplyString;
    }
}
