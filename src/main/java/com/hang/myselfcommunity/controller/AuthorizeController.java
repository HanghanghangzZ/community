package com.hang.myselfcommunity.controller;

import com.hang.myselfcommunity.dto.AccessTokenDTO;
import com.hang.myselfcommunity.dto.GitHubUser;
import com.hang.myselfcommunity.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    private GitHubProvider gitHubProvider;
    @Autowired
    public void setGitHubProvider(GitHubProvider gitHubProvider) {
        this.gitHubProvider = gitHubProvider;
    }

    /**
     * 用户认证后GitHub携带code执行的回调函数
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("86996cfecf7f8dcf255a");
        accessTokenDTO.setClient_secret("295e4b42539d546fbdd2472b48206787bd40a1c4");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser user = gitHubProvider.getUser(accessToken);
        System.out.println(user);
        return "index";
    }


}
