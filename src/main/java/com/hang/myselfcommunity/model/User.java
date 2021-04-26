package com.hang.myselfcommunity.model;

public class User {
    private Integer ID;
    private String account_id;
    private String name;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;

    public User() {
    }

    public User(Integer ID, String account_id, String name, String token, Long gmt_create, Long gmt_modified) {
        this.ID = ID;
        this.account_id = account_id;
        this.name = name;
        this.token = token;
        this.gmt_create = gmt_create;
        this.gmt_modified = gmt_modified;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Long gmt_create) {
        this.gmt_create = gmt_create;
    }

    public Long getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(Long gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", account_id='" + account_id + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", gmt_create=" + gmt_create +
                ", gmt_modified=" + gmt_modified +
                '}';
    }
}
