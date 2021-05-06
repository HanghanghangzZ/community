package com.hang.myselfcommunity.mapper;

import com.hang.myselfcommunity.model.Question;
import com.hang.myselfcommunity.model.QuestionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface QuestionExtMapper {

    int increaseView(Question record);
}