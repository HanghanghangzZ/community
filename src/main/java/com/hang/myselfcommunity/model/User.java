package com.hang.myselfcommunity.model;

import lombok.Data;

@Data
public class User {
    private Integer ID;
    private String accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
