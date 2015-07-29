package io.rong.app.model;

import java.io.Serializable;

/**
 * Created by Bob on 2015/3/4.
 */
public class ApiResult implements Serializable {

    /**
     * id，用户userid，群组 groupid
     */
    private String id;
    /**
     *用户名
     */
    private String username;
    /**
     *用户头像
     */
    private String portrait;
    /**
     *状态
     */
    private int status;
    /**
     *token值
     */
    private String token;
    /**
     *群组名称
     */
    private String name;
    /**
     *群组介绍
     */
    private String introduce;
    /**
     *群组当前人数
     */
    private String number;
    /**
     *群组最大人数值
     */
    private String max_number;
    /**
     *群组创建人 id
     */
    private String create_user_id;
    /**
     *群组创建时间
     */
    private String creat_datetime;

    private int env;

    public int getEnv() {
        return env;
    }

    public void setEnv(int env) {
        this.env = env;
    }

    public ApiResult() {

    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMax_number() {
        return max_number;
    }

    public void setMax_number(String max_number) {
        this.max_number = max_number;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreat_datetime() {
        return creat_datetime;
    }

    public void setCreat_datetime(String creat_datetime) {
        this.creat_datetime = creat_datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {

        return username;

    }

    public void setUsername(String username) {

        this.username = username;

    }

    public String getPortrait() {

        return portrait;

    }

    public void setPortrait(String portrait) {

        this.portrait = portrait;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
