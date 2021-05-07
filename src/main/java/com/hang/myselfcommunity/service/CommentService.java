package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.enums.CommentTypeEnum;
import com.hang.myselfcommunity.exception.CustomizeErrorCode;
import com.hang.myselfcommunity.exception.CustomizeException;
import com.hang.myselfcommunity.mapper.CommentExtMapper;
import com.hang.myselfcommunity.mapper.CommentMapper;
import com.hang.myselfcommunity.mapper.QuestionExtMapper;
import com.hang.myselfcommunity.mapper.QuestionMapper;
import com.hang.myselfcommunity.model.Comment;
import com.hang.myselfcommunity.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
