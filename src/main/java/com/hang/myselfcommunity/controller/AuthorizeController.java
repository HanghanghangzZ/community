package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.AccessTokenDTO;
import com.hang.myselfcommunity.dto.GitHubUser;
import com.hang.myselfcommunity.mapper.UserMapper;
import com.hang.myselfcommunity.model.User;
import com.hang.myselfcommunity.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    private GitHubProvider gitHubProvider;
    @Autowired
    public void setGitHubProvider(GitHubProvider gitHubProvider) {
        this.gitHubProvider = gitHubProvider;
    }

    private UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /* 在Spring的IOC容器启动时会将配置文件中的键值对存到一个map中，当真正要使用的时候，通过下面这个注解去获取，设置到我们的属性中 */
    /* 像下面这样单独的提取出来是为了在不同的部署环境有不同的配置 */
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    /**
     * 用户认证后GitHub携带code执行的回调函数
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
        System.out.println(gitHubUser);
        if (gitHubUser != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getLogin());
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());

            userMapper.insertUser(user);
            /* 保存cookie */
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            /* 登录失败，重新登录 */
            return "redirect:/";
        }
    }




}
