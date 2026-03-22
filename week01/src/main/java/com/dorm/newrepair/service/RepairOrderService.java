package com.dorm.newrepair.service;

import com.dorm.newrepair.mapper.RepairOrderMapper;
import com.dorm.newrepair.model.RepairOrder;
import com.dorm.newrepair.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RepairOrderService {
    //学生：
    //创建报修单
    public boolean createOrder(RepairOrder order) {
        //通过时间戳 随机生成报修单号
        String orderId = "GDUT" + System.currentTimeMillis();
        order.setOrderId(orderId);
        order.setStatus(0);//设置成待处理
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.insertRepairOrder(order) > 0;
        }
    }

    //取消报修单
    public boolean cancelOrder(String orderId) {
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            //先查询这个orderId对应的报修单存不存在
            RepairOrder order = mapper.selectByOrderId(orderId);
            if (order == null) {
                return false;
            }
            return mapper.cancelOrder(orderId) > 0;
        }
    }

    // 学生查看自己的报修记录（封装业务逻辑，调用Mapper）
    public List<RepairOrder> getMyOrders(String studentAccount) {
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            // 调用Mapper接口的selectByStudentAccount方法，返回结果
            return mapper.selectByStuAccount(studentAccount);
        }
    }

    //管理员：
    //修改报修单状态
    public static boolean updateOrderStatus(String orderId, Integer status) {
        //首先检验状态是否合法 就是0 待处理 1 处理中 2 已完成
        if (status != 0 && status != 1 && status != 2) {
            System.out.println("当前状态不合法！仅支持0-待处理 1-处理中 2-已完成");
            return false;
        }
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            //再校验报修单是否存在
            if (mapper.selectByOrderId(orderId) == null) {
                return false;
            }
            return mapper.updateOrderStatus(orderId, status) > 0;

        }
    }

    //查询所有报修单
    public static List<RepairOrder> selectOrder(Integer status) {
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.selectOrder(status);
        }
    }

    //查看报修单详情
    public static RepairOrder selectOrderDetail(String orderId) {
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            return mapper.selectByOrderId(orderId);
        }
    }

    //删除报修单
    public boolean deleteOrder(String orderId) {
        try (SqlSession session = MybatisUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            if (mapper.selectByOrderId(orderId) == null) {
                System.out.println("报修单不存在!");
                return false;
            }
            return mapper.deleteOrder(orderId) > 0;
        }
    }
}