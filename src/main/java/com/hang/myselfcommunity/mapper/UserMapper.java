package com.hang.myselfcommunity.mapper;

import com.hang.myselfcommunity.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /* 这里的 #{} 是通过调用对应类的get方法来获得的 */
    @Insert("INSERT INTO community.user(ID,account_id,name,token,gmt_create,gmt_modified) VALUES(#{ID},#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified})")
    void insertUser(User user);

    /* 非引用类型的参数前要加上@Param */
    @Select("SELECT * FROM community.user WHERE token=#{token}")
    User findByToken(@Param("token") String token);
}
