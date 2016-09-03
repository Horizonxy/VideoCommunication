package com.av.communication.model;

/**
 * 自己的状态数据
 */
public class MySelfInfo {

    private String identifier;
    private String userSig;

    private static MySelfInfo instance = new MySelfInfo();

    public static MySelfInfo getInstance() {
        return instance;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}