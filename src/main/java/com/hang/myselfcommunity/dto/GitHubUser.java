package com.hang.myselfcommunity.dto;

import lombok.Data;

@Data
public class GitHubUser {
    private String login;
    private long id;
    private String bio;
    private String avatar_url;
}
