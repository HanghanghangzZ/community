package com.hang.myselfcommunity.mapper;

import com.hang.myselfcommunity.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentExtMapper {

    int increaseCommentCount(Comment record);
}
