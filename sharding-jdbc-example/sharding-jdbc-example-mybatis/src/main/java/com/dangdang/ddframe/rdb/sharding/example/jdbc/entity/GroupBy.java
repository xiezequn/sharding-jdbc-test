package com.dangdang.ddframe.rdb.sharding.example.jdbc.entity;

/**
 * Created with IntelliJ IDEA.
 * User: xiezq
 * Date: 2017/1/11
 * Depiction:
 */
public class GroupBy {
    private int userId;
    private int countUser;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCountUser() {
        return countUser;
    }

    public void setCountUser(int countUser) {
        this.countUser = countUser;
    }
}
