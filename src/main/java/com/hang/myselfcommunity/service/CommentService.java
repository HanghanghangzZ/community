package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.dto.CommentDTO;
import com.hang.myselfcommunity.enums.CommentTypeEnum;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.exception.CustomizeException;
import com.hang.myselfcommunity.mapper.*;
import com.hang.myselfcommunity.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class CommentService {
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

    private QuestionExtMapper questionExtMapper;

    @Autowired
    public void setQuestionExtMapper(QuestionExtMapper questionExtMapper) {
        this.questionExtMapper = questionExtMapper;
    }

    private CommentExtMapper commentExtMapper;

    @Autowired
    public void setCommentExtMapper(CommentExtMapper commentExtMapper) {
        this.commentExtMapper = commentExtMapper;
    }

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional  //为此方法添加事务支持，当它抛出 RuntimeException 或 Error 时就会回滚
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == 0 || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            /* 回复评论 */
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            dbComment.setCommentCount(1);
            commentExtMapper.increaseCommentCount(dbComment);
        } else {
            /* 回答问题 */
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);

            dbQuestion.setCommentCount(1);
            questionExtMapper.increaseCommentCount(dbQuestion);
        }
    }

    /**
     * 这里大量的用到了Java8的新特性，stream流。那么为什么要这么做呢？
     * 我们在这个方法中需要将Comment与发布它的User一起封装成CommentDTO。
     * 因为一个User可以在一个Question下发布多个Comment，
     * 如果使用for循环，在每一次循环中查询该次循环中Comment的发布人，会发生重读查询的情况。
     * 所以我们在这个方法中使用stream流来做处理。将sql层面上的处理转移到Java代码层面上，减少重复操作。
     *
     * @param id
     * @return
     */
    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        /* map 方法用于映射每个元素到对应的结果,执行comments这个列表中每个元素的getCommentator()方法，并将结果抽取成一个Set */
        /* lambda表达式中的方法如果只有一个参数，而且这个类型的参数可以推导得出，那么可以忽略小括号 */
        /* 如： (Comment comment) -> comment.getCommentator() 可以写成下面这样 */
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(new ArrayList<>(commentators));
        List<User> users = userMapper.selectByExample(userExample);
        /* 将users这个列表中的元素转换成一个map，其中键为user的id，值为user本身 */
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        /* 将comments列表中的每一个comment元素结合前面获得的发布人Map封装成commentDTO */
        List<CommentDTO> collectDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO(comment, userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return collectDTOs;
    }
}
