package com.hongenit.gifshowapp;

/**
 * Created by Xiaohong on 2019-06-28.
 * desc:
 */
public class User {

    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;


    /**
     * user_id : 11725
     * nickname : 普通人--
     * followings_count : 0
     * followers_count : 0
     * feeds_count : 1
     * is_following : false
     * description :
     * gender : 0
     * birthday : 1561448659000
     * avatar : http://res.quxianggif.com/avatar/20190626205013-9F17D52C26DC4258BFCEB06C984C6D27?imageView2/1/w/180/h/180
     * account : 123@126.com
     * pwd : 75256CA278FE3AFECCE17A853F283498
     * token : 75256CA278FE3AFECCE17A853F283498
     */

    private String user_id;
    private String nickname = "";
    private int followings_count;
    private int followers_count;
    private int feeds_count;
    private boolean is_following;
    private String description = "";
    private int gender;
    private String birthday;
    private String avatar;
    private String account;
    private String pwd;
    private String token;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        if (nickname == null) {
            nickname = "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFollowings_count() {
        return followings_count;
    }

    public void setFollowings_count(int followings_count) {
        this.followings_count = followings_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFeeds_count() {
        return feeds_count;
    }

    public void setFeeds_count(int feeds_count) {
        this.feeds_count = feeds_count;
    }

    public boolean isIs_following() {
        return is_following;
    }

    public void setIs_following(boolean is_following) {
        this.is_following = is_following;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                ", followings_count=" + followings_count +
                ", followers_count=" + followers_count +
                ", feeds_count=" + feeds_count +
                ", is_following=" + is_following +
                ", description='" + description + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", avatar='" + avatar + '\'' +
                ", account='" + account + '\'' +
                ", pwd='" + pwd + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
