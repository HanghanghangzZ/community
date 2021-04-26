package com.hang.myselfcommunity.mapper;

import com.hang.myselfcommunity.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /* 这里的 #{} 是通过调用对应类的get方法来获得的 */
    @Insert("INSERT INTO community.user VALUES(#{ID},#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified})")
    void insertUser(User user);
}
