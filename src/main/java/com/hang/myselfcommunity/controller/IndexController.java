 package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        /* 利用cookie来保持登录状态 */
        /* 这只适用于小用户的范围，当用户量较大时，这样子根据token进行查询的方式速度较为缓慢 */
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null) {
                    /* 登录成功，保存session */
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        return "index";
    }
}
