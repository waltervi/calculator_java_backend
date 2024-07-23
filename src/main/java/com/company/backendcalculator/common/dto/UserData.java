package com.company.backendcalculator.common.dto;

public class UserData {
    private long userId;
    private long endTimeStamp;
    private String tokenKey;

    public UserData(){}

    public UserData(long userId, long endTimeStamp, String tokenKey) {
        this.userId = userId;
        this.endTimeStamp = endTimeStamp;
        this.tokenKey = tokenKey;
    }

    public long getUserId() {
        return userId;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public String getTokenKey() {
        return tokenKey;
    }
}
