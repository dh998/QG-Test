package com.dorm.newrepair.controller;


import com.dorm.newrepair.mapper.RepairOrderMapper;
import com.dorm.newrepair.model.RepairOrder;
import com.dorm.newrepair.service.RepairOrderService;
import com.dorm.newrepair.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair-orders")
public class RepairOrderController {
    @Autowired
    private RepairOrderService repairOrderService;

    //1.创建报修单(POST/repair-orders)
    @PostMapping
    public Result<Boolean> createOrder(@RequestBody RepairOrder order) {
        boolean result = repairOrderService.createOrder(order);
        return Result.success(result);
    }

    //2.取消报修单
    @PatchMapping("/{orderId}/cancel")
    public Result<Boolean> cancelOrder(@PathVariable String orderId) {
        boolean result = repairOrderService.cancelOrder(orderId);
        return Result.success(result);
    }

    //3.查询我的报修单 （学生）
    @GetMapping("/myOrders")
    public Result<List<RepairOrder>> getMyOrders(@RequestParam String studentAccount) {
        List<RepairOrder> list = repairOrderService.getMyOrders(studentAccount);
        return Result.success(list);
    }

    //4.更新状态（管理员）
    @PatchMapping("/{orderId}/status")
    public Result<Boolean> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam Integer status) {
        boolean result = repairOrderService.updateOrderStatus(orderId, status);
        return Result.success(result);
    }

    //获取报修单列表 根据状态
    @GetMapping
    public Result<List<RepairOrder>> getOrderList(@RequestParam Integer status) {
        List<RepairOrder> list = repairOrderService.selectOrder(status);
        return Result.success(list);
    }

    //
    @GetMapping("/{orderId}")
    public Result<RepairOrder> getOrderDetail(@RequestParam String orderId) {
        RepairOrder result =  repairOrderService.selectOrderDetail(orderId);
        return Result.success(result);
    }

    @DeleteMapping ("/{orderId}")
    public Result<Boolean> deleteOrder(@RequestParam String orderId) {
        Boolean result = repairOrderService.deleteOrder(orderId);
        return Result.success(result);
    }
}
