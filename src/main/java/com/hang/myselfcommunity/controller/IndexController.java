package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.PaginationDTO;
import com.hang.myselfcommunity.dto.QuestionDTO;
import com.hang.myselfcommunity.mapper.QuestionMapper;
import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.Question;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private QuestionService questionService;
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size) {
        /* 利用cookie来保持登录状态 */
        /* 这只适用于小用户的范围，当用户量较大时，这样子根据token进行查询的方式速度较为缓慢 */
        Cookie[] cookies = request.getCookies();
        /* 防止用户把cookie全部删除导致空指针异常 */
        if (cookies != null && cookies.length != 0) {
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
        }
        /* 分页展示问题 */
        PaginationDTO paginationDTO = questionService.list(page, size);
        model.addAttribute("paginationDTO", paginationDTO);
        return "index";
    }
}
