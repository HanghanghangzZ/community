package com.hang.myselfcommunity.dto;

import lombok.Data;

/**
 * 作为Comment在网络传输中，作为被传递的类使用。
 * CommentDTO作为在网络传输中被返回的类使用
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
