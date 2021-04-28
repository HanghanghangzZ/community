package com.hang.myselfcommunity.mapper;

import com.hang.myselfcommunity.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO community.question(title,description,gmt_create,gmt_modified,creator,tag) VALUES(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("SELECT * FROM community.question LIMIT #{offset},#{size}")
    List<Question> list(@Param("offset") int offset, @Param("size") Integer size);

    @Select("SELECT COUNT(1) FROM community.question")
    Integer count();

    @Select("SELECT * FROM community.question WHERE creator=#{userId} LIMIT #{offset}, #{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param("offset") int offset, @Param("size") Integer size);

    @Select("SELECT COUNT(1) FROM community.question WHERE creator=#{userId}")
    Integer countByUserId(@Param("userId") Integer userId);
}
