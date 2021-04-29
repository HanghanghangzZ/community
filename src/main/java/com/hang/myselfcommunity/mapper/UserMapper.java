package com.hang.myselfcommunity.mapper;

import com.hang.myselfcommunity.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    /* 这里的 #{} 是通过调用对应类的get方法来获得的 */
    @Insert("INSERT INTO community.user(ID,account_id,name,token,gmt_create,gmt_modified,avatar_url) VALUES(#{ID},#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insertUser(User user);

    /* 非引用类型的参数前要加上@Param */
    @Select("SELECT * FROM community.user WHERE token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("SELECT * FROM community.user WHERE id=#{id}")
    User findById(@Param("id") Integer creator);

    @Select("SELECT * FROM community.user WHERE account_id=#{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("UPDATE community.user SET token=#{token}, name=#{name}, avatar_url=#{avatarUrl}, gmt_modified=#{gmtModified}")
    void update(User user);
}
