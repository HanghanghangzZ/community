package com.hang.myselfcommunity.provider;

import com.alibaba.fastjson.JSON;
import com.hang.myselfcommunity.dto.AccessTokenDTO;
import com.hang.myselfcommunity.dto.GitHubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GitHubProvider {

    /**
     * 携带code去调用GitHub的api来获取access_token
     *
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            /* responseBody会返回一个类似下面这样的字符串，其中包含了access_token */
            /* access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&token_type=bearer */
            String access_token = responseBody.split("&")[0].split("=")[1];

            log.info("getAccessToken responseBody, {}", responseBody);
            log.info("getAccessToken access_token, {}", access_token);

            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用access_token来调用GitHub的api获取用户信息
     *
     * @param accessToken
     * @return
     */
    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)     //使用请求头的方式调用
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println(responseBody);
            /* 将json转换为Java的类对象 */
            GitHubUser gitHubUser = JSON.parseObject(responseBody, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
