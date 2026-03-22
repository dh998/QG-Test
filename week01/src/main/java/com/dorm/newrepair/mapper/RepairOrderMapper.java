package com.dorm.newrepair.mapper;

import com.dorm.newrepair.model.RepairOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RepairOrderMapper {
    //1.学生创建报修单
    int insertRepairOrder(RepairOrder repairOrder);
    //2.根据学生账号查询报修记录
    List<RepairOrder> selectByStuAccount(@Param("studentAccount")String account);
    //3.取消报修单
    int cancelOrder(@Param("orderId")String orderId);
    //4.管理员修改报修单状态
    int updateOrderStatus(@Param("orderId") String orderId,@Param("status") Integer status);
    //5.管理员删除报修单
    int deleteOrder(@Param("orderId") String orderId);
    //6.管理员查询所有报修单
    List<RepairOrder> selectOrder(@Param("status") Integer status);
    //7.根据报修单号查询
    RepairOrder selectByOrderId(@Param("orderId") String orderId);
}
