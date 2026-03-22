package com.dorm.newrepair.model;

import lombok.Data;

import java.util.Date;

//这里是我之前下载的lombok插件 在这里就不用手动创建get set方法和 构造函数了
@Data
public class User {
    private String account;
    private String password;
    private String name;
    private Integer role;
    private String building;
    private String roomNum;
    private Date createTime;
}
