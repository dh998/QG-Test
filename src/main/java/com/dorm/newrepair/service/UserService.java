package com.dorm.newrepair.service;

import com.dorm.newrepair.mapper.UserMapper;
import com.dorm.newrepair.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public  class UserService {
    @Autowired
    private UserMapper userMapper;

    //用户业务逻辑: 用户注册 登录 修改密码 绑定宿舍
    public  boolean register(User user) {
        if (!checkAccount(user.getAccount(), user.getRole())) {
            System.out.println("账号格式错误，学生以3125或者3225开头，管理员以0025开头");
            return false;
        }
//        try (SqlSession session = MybatisUtil.getSqlSession()) {
//            UserMapper mapper = session.getMapper(UserMapper.class);
//            if (mapper.selectUserByAccount(user.getAccount()) != null) {
//                System.out.println("该账号已被创建！");
//                return false;
//            }
//            return mapper.insertUser(user) > 0;
//            //我们写insertUser方法用int 就是为了通过这种方式
//        }
        // 直接调用 mapper 方法，Spring 自动管理事务和连接
        if (userMapper.selectUserByAccount(user.getAccount()) != null) {
            System.out.println("该账号已被创建！");
            return false;
        }

        return userMapper.insertUser(user) > 0;
    }

    public  User login(String account, String password) {
//        try (SqlSession session = MybatisUtil.getSqlSession()) {
//            UserMapper mapper = session.getMapper(UserMapper.class);
//            User user = mapper.selectUserByAccount(account);
        User user = userMapper.selectUserByAccount(account);
        //校验账号是否存在 且 密码是否正确
        if (user == null || ! user.getPassword().equals(password)) {
            return null;
        } else {
            return user;
        }
    }


public boolean updatePassword(String account, String oldPassword, String newPassword) {
//    try (SqlSession session = MybatisUtil.getSqlSession()) {
//        UserMapper mapper = session.getMapper(UserMapper.class);
//        User user = mapper.selectUserByAccount(account);
        User user = userMapper.selectUserByAccount(account);
        //校验该账号密码和输入密码是否一致
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return false;
        } else {
            //如果正确 就把密码改成输入的新密码
            return userMapper.updatePassword(account, newPassword) > 0;
        }
    }


//学生绑定/修改宿舍
public boolean updateDorm(String account, String building, String roomNum) {
//    try (SqlSession session = MybatisUtil.getSqlSession()) {
//        UserMapper mapper = session.getMapper(UserMapper.class);
        int rows = userMapper.updateDorm(account, building, roomNum);
        return rows > 0;
    }


//内部验证学生还是员工
private boolean checkAccount(String account, Integer role) {
    if (account == null || role == null) {
        return false;
    }
    // 学生：3125或3225开头
    if (role == 1) {
        return account.startsWith("3125") || account.startsWith("3225");
    }
    // 管理员：0025开头
    else if (role == 2) {
        return account.startsWith("0025");
    }
    return false;
    }
}