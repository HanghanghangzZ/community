package com.hang.myselfcommunity.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "您找的问题不存在了，要不要换个试试 (*^_^*)"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NOT_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004, "服务冒烟了，要不您稍后再试试!!!"),
    TYPE_PARAM_WRONG(2005, "类型参数错误"),
    COMMENT_NOT_FOUND(2006, "找不到您要回复的评论"),
    COMMENT_IS_EMPTY(2007, "您的评论不能为空"),
    READ_NOTIFICATION_FAIL(2008, "哥们！搁着看别人信息呢 (●'◡'●)"),
    NOTIFICATION_NOT_FOUND(2009, "这个通知不翼而飞了");
    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
