package com.hang.myselfcommunity.dto;

import lombok.Data;

/**
 * 注意这个类中的属性名必须和文档中要求的属性一致 https://docs.github.com/en/developers/apps/authorizing-oauth-apps#redirect-urls
 * 不然将其转换为json后发送post请求调用https://github.com/login/oauth/access_token会无法访问
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
