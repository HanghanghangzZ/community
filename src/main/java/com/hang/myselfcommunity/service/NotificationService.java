package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.dto.NotificationDTO;
import com.hang.myselfcommunity.dto.PaginationDTO;
import com.hang.myselfcommunity.enums.NotificationStatusEnum;
import com.hang.myselfcommunity.enums.NotificationTypeEnum;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.exception.CustomizeException;
import com.hang.myselfcommunity.mapper.CommentMapper;
import com.hang.myselfcommunity.mapper.NotificationMapper;
import com.hang.myselfcommunity.mapper.QuestionMapper;
import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private NotificationMapper notificationMapper;

    @Autowired
    public void setNotificationMapper(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private QuestionMapper questionMapper;

    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    private CommentMapper commentMapper;

    @Autowired
    public void setCommentMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    /**
     * 将指定用户的消息通知封装成PaginationDTO
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO listByUserId(Long userId, Integer page, Integer size) {
        /* 容错 */
        Integer totalPage;

        /* 获取指定user所接收到的消息总数 */
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page <= 0) {
            page = 1;
        } else if (page > totalPage && totalPage != 0) {
            page = totalPage;
        }

        int offset = size * (page - 1);

        /* 分页查询指定user的Notification */
        NotificationExample notificationExample1 = new NotificationExample();
        notificationExample1.createCriteria()
                .andReceiverEqualTo(userId);
        notificationExample1.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(notificationExample1, new RowBounds(offset, size));
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        if (notificationList.size() == 0) {
            return paginationDTO;
        }

        /* 获取指定user */
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(userId);
        List<User> users = userMapper.selectByExample(userExample);

        /* 检索出所有的通知者 */
        List<Long> notifierList = notificationList.stream().map(Notification::getNotifier).distinct().collect(Collectors.toList());
        UserExample userExample1 = new UserExample();
        userExample1.createCriteria()
                .andIdIn(notifierList);
        List<User> notifierUserList = userMapper.selectByExample(userExample1);
        /* 将notifierUserList这个列表中的元素转换成一个map，其中键为user的id，值为user本身 */
        Map<Long, User> userMap = notifierUserList.stream().collect(Collectors.toMap(User::getId, user -> user));

        /* 检索出问题的title 或 评论的内容的前十个字符为值。以Notification对应的id为键 */
        Map<Long, String> outTitleMap = notificationList.stream().collect(Collectors.toMap(Notification::getId, notification -> {
            if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {
                Question question = questionMapper.selectByPrimaryKey(notification.getOuterId());
                return question.getTitle();
            } else {
                Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterId());
                String content = comment.getContent();
                if (content.length() > 10) {
                    content = content.substring(0, 10) + "... ...";
                }
                return content;
            }
        }));

        /* 将Notification 封装为 NotificationDTO */
        ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notificationList) {
            String typeReplyString;
            String outerTitle = outTitleMap.get(notification.getId());
            if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {
                typeReplyString = NotificationTypeEnum.REPLY_QUESTION.getContent();
            } else {
                typeReplyString = NotificationTypeEnum.REPLY_COMMENT.getContent();
            }
            NotificationDTO notificationDTO = new NotificationDTO(notification,
                    userMap.get(notification.getNotifier()),
                    outerTitle,
                    typeReplyString);
            notificationDTOS.add(notificationDTO);
        }
        /* 封装成分页使用的PaginationDTO */
        paginationDTO.setDTOList(notificationDTOS);
        paginationDTO.setPaginationDTO(totalPage, page);

        return paginationDTO;
    }

    public long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andIdEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    /**
     * 在用户读取通知时，进行校验
     *
     * @param id
     * @param user
     * @return
     */
    public Notification read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        return notification;
    }
}
