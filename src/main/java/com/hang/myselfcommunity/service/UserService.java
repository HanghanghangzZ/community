package com.hang.myselfcommunity.service;

import com.hang.myselfcommunity.dto.GitHubUser;
import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户第一次登录就在user表中create
     * 用户不是第一次登录就在user表中update
     * <p>
     * 用户每一次登录都会更新cookie以及user表中的token
     *
     * @param user
     */
    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser != null) {
            /* 更新 */
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setGmtModified(System.currentTimeMillis());
            userMapper.update(dbUser);
        } else {
            /* 插入 */
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            userMapper.insertUser(user);
        }
    }
}
