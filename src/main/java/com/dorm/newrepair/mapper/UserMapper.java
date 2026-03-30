package com.dorm.newrepair.mapper;

import com.dorm.newrepair.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    //1.注册用户
    int insertUser(User user);
    //2.根据账号查询用户
    User selectUserByAccount(@Param("account") String account);
    //3.修改密码
    int updatePassword(@Param("account") String account,@Param("newPassword") String newPassword);
    //4.学生绑定/修改宿舍 因为初次绑定宿舍跟修改宿舍的流程基本一致 所以就放在一块了
    int updateDorm(@Param("account") String account,@Param("building")String building,@Param("roomNum")String roomNum);
}
