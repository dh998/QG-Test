package com.dorm.newrepair;

import com.dorm.newrepair.model.RepairOrder;
import com.dorm.newrepair.model.User;
import com.dorm.newrepair.service.RepairOrderService;
import com.dorm.newrepair.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


public class Main {
    // 静态Scanner：全局唯一，避免重复创建
    private static final Scanner sc = new Scanner(System.in);
    // 业务层对象：全局调用
    private static final UserService userService = new UserService();
    private static final RepairOrderService orderService = new RepairOrderService();
    // 登录后当前用户：全局共享
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            showMainMenu();
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    //登录
                    login();
                    break;
                case 2:
                    //注册
                    register();
                    break;
                case 3:
                    //退出
                    System.out.println("感谢使用宿舍报修服务系统 再见！");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入错误！请输入1到3的数字");
            }
        }
    }

    //  public static void showMainMenu() {
//        System.out.println("===========================");
//        System.out.println(" \uD83C\uDFE0宿舍报修管理系统");
//        System.out.println("===========================");
//        System.out.println("1.登录");
//        System.out.println(" 2.注册");
//        System.out.println(" 3.退出");
//        System.out.println(" 请选择操作（输⼊ 1-3）:");
//    }
    public static void showMainMenu() {
        System.out.println("===========================");
        System.out.println(" \uD83C\uDFE0宿舍报修管理系统");
        System.out.println("===========================");
        System.out.println(" 1.登录");
        System.out.println(" 2.注册");
        System.out.println(" 3.退出");
        System.out.println(" 请选择操作（输⼊ 1-3）:");
    }
    //登录
    private static void register(){
        System.out.println("=====  ⽤⼾注册  =====");
        System.out.println("请选择⻆⾊（ 1 学⽣， 2 维修⼈员）：");
        int role = sc.nextInt();
        if(role!=1 && role!=2){
            System.out.println("角色选择错误 请输入1-学生 2-维修人员");
            return;
        }
        System.out.println("请输入账号：");
        String account = sc.next();
        System.out.println("请输入密码：");
        String pwd = sc.next();
        System.out.println("请确认密码：");
        String confirmpwd = sc.next();
        if(!pwd.equals(confirmpwd)){
            System.out.println("两次密码输入不一致！请重新输入");
            return;
        }
        System.out.println("请输入姓名");
        String name = sc.next();
        //封装user对象
        User user = new User();
        user.setAccount(account);
        user.setPassword(pwd);
        user.setRole(role);
        user.setName(name);
        // 新增：校验姓名不为空
        if (name == null || name.trim().isEmpty()) {
            System.out.println("姓名不能为空！注册失败");
            return;
        }

        //调用业务层注册
        if(UserService.register(user)){
            System.out.println("注册成功!请返回主界面登录");
        }else{
            System.out.println("注册失败!");//错误原因在register里已经写好了 不用再写
        }
    }

    private static void login(){
        System.out.println("=====用户登录=====");
        System.out.println("请输入账号：");
        String account = sc.next();
        System.out.println("请输入密码:");
        String pwd = sc.next();
       //调用UserService的login方法登录
       User user = UserService.login(account,pwd);
        if(user == null){
            System.out.println("登录失败！账号或密码错误");
            return; //一定要加
        }
        //保存当前登录用户
        currentUser = user;
        System.out.println("登录成功！角色："+(user.getRole()== 1?"学生":"管理员"));
        if(user.getRole()==1){
            if(user.getBuilding()==null || user.getRoomNum()==null){
                System.out.println("building: " + currentUser.getBuilding() + ", roomNum: " + currentUser.getRoomNum());
                System.out.println("【提示】：你尚未绑定宿舍，请先绑定宿舍！");
                bindDorm();
            }else{
                studentMenu();
            }
        }else{
            adminMenu();
        }
    }
    //绑定宿舍
    public static void bindDorm() {
        System.out.println("===== 绑定宿舍 =====");
        System.out.println("请输入宿舍楼栋：");
        String building = sc.next();
        System.out.println("请输入房间号：");
        String roomNum = sc.next();
        boolean success = UserService.updateDorm(currentUser.getAccount(), building, roomNum);
        if (success) {
            System.out.println("宿舍绑定成功！");
            //更新用户信息
            currentUser.setBuilding(building);
            currentUser.setRoomNum(roomNum);
        }else{
            System.out.println("宿舍绑定失败！");
        }
    }

    //学生菜单
    public static void studentMenu(){
        while(true) {
            System.out.println("===== 学生菜单 =====");
            System.out.println("1. 绑定/修改宿舍");
            System.out.println("2. 创建报修单");
            System.out.println("3. 查看我的报修记录");
            System.out.println("4. 取消报修单");
            System.out.println("5. 修改密码");
            System.out.println("6. 退出");
            System.out.println("请选择操作（输入1-6）：");
            int choose = sc.nextInt();
            switch (choose) {
                case 1:
                    bindDorm();
                    break;
                case 2:
                    createOrder();
                    break;
                case 3:
                    showMyOrders();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    updatePwd();
                    break;
                case 6:
                    System.out.println("退出学生端，返回主菜单！");
                    currentUser = null;
                    return;
                default:
                    System.out.println("输入格式错误！请输入1到6的数字");
            }
        }
    }
    //创建报修单
    public static void createOrder(){
        System.out.println("===== 创建报修单 =====");
        System.out.print("请输入故障类型（如水电故障、家具故障、其他）：");
        String faultType = sc.next();
        System.out.println("请输入故障详细描述：");
        String faultdesc = sc.next();
        //封装报修单对象
        RepairOrder order =  new RepairOrder();
        order.setStudentAccount(currentUser.getAccount());
        order.setBuilding(currentUser.getBuilding());
        order.setRoomNum(currentUser.getRoomNum());
        order.setFaultType(faultType);
        order.setFaultDesc(faultdesc);
        order.setCreateTime(LocalDateTime.now()); // 用LocalDateTime
        //调用业务层
        if(orderService.createOrder(order)){
            System.out.println("报修单创建成功！报修单编号为："+order.getOrderId());
        }else{
            System.out.println("报修单创建失败");
        }
    }

    //查询报修记录
    public static void showMyOrders(){
        System.out.println("=====我的报修记录=====");
        List<RepairOrder> orderList = orderService.getMyOrders(currentUser.getAccount());
        if(orderList == null||orderList.isEmpty()){
            System.out.println("暂无报修记录!");
            return;
        }
        for (int i = 0; i < orderList.size(); i++) {
            RepairOrder order = orderList.get(i);
            System.out.println((i+1)+"-编号:"+order.getOrderId()
                              +"|故障类型:"+order.getFaultType()
                              + " | 状态：" + getStatusDesc(order.getStatus())
                              + " | 创建时间：" + order.getCreateTime());
        }
        System.out.println("是否查看详情 （1-是 0-否）：");
        int choice = sc.nextInt();
        if(choice == 1){
        System.out.println("请输入要查看的报修单编号");
        String orderId = sc.next();
        showOrderDetail(orderId);
        }

        //查看详情后，等待用户按回车，再返回主菜单
        System.out.println("\n按回车键返回主菜单...");
        sc.nextLine();
        sc.nextLine();
    }
    //展示报修单详情
    private static void showOrderDetail(String orderId) {
        RepairOrder order = orderService.selectOrderDetail(orderId);
        if (order == null) {
            System.out.println("报修单不存在！");
            return;
        }
        System.out.println("\n===== 报修单详情 =====");
        System.out.println("报修单编号：" + order.getOrderId());
        System.out.println("报修学生：" + order.getStudentAccount());
        System.out.println("报修宿舍：" + order.getBuilding() + "-" + order.getRoomNum());
        System.out.println("故障类型：" + order.getFaultType());
        System.out.println("故障描述：" + order.getFaultDesc());
        System.out.println("当前状态：" + getStatusDesc(order.getStatus()));
        System.out.println("创建时间：" + order.getCreateTime());
        System.out.println("最后修改时间：" + order.getUpdateTime());
        System.out.println("====================");
    }

    //通用：状态码转文字描述
    private static String getStatusDesc(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "待处理";
            case 1:
                return "处理中";
            case 2:
                return "已完成";
            case 3:
                return "已取消";
            default:
                return "未知";
        }
    }

    // 8. 学生取消报修单
    private static void cancelOrder() {
        System.out.println("\n===== 取消报修单 =====");
        System.out.print("请输入要取消的报修单编号：");
        String orderId = sc.next().trim();
        System.out.println("确认取消？仅待处理状态可取消（1-确认，0-取消）：");
        int choice = getIntInput("请选择：");
        if (choice == 1) {
            if (orderService.cancelOrder(orderId)) {
                System.out.println("报修单取消成功！");
            } else {
                System.out.println("报修单取消失败！（非待处理状态或编号不存在）");
            }
        } else {
            System.out.println("已取消操作！");
        }
    }

    // 通用：修改密码
    private static void updatePwd() {
        System.out.println("\n===== 修改密码 =====");
        System.out.print("请输入原密码：");
        String oldPwd = sc.next().trim();
        System.out.print("请输入新密码：");
        String newPwd = sc.next().trim();
        System.out.print("请确认新密码：");
        String confirmPwd = sc.next().trim();
        if (!newPwd.equals(confirmPwd)) {
            System.out.println("两次新密码输入不一致！");
            return;
        }
        if (userService.updatePassword(currentUser.getAccount(), oldPwd, newPwd)) {
            System.out.println("密码修改成功！请重新登录");
            currentUser = null; // 清空用户，返回主菜单
        } else {
            System.out.println("密码修改失败！原密码错误");
        }
    }

    //管理员菜单
    private static void adminMenu() {
        while (true) {
            System.out.println("\n===== 管理员菜单 =====");
            System.out.println("1. 查看所有报修单");
            System.out.println("2. 查看报修单详情");
            System.out.println("3. 更新报修单状态");
            System.out.println("4. 删除报修单");
            System.out.println("5. 修改密码");
            System.out.println("6. 退出");
            int choice = getIntInput("请选择操作（输入1-6）：");
            switch (choice) {
                case 1:
                    showAllOrders();
                    break;
                case 2:
                    System.out.print("请输入要查看的报修单编号：");
                    String orderId = sc.next().trim();
                    showOrderDetail(orderId);
                    break;
                case 3:
                    updateOrderStatus();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    updatePassword();
                    break;
                case 6:
                    System.out.println("退出管理员端，返回主菜单！");
                    currentUser = null;
                    return;
                default:
                    System.out.println("输入错误！请输入1-6的数字");
            }
        }
    }

    //管理员查看所有报修单（支持按状态筛选）
    private static void showAllOrders() {
        System.out.println("\n===== 查看所有报修单 =====");
        System.out.println("请选择筛选条件：");
        System.out.println("0-待处理  1-处理中  2-已完成  3-已取消  4-全部");
        int statusChoice = getIntInput("请选择（输入0-4）：");
        Integer status = null;
        if (statusChoice >= 0 && statusChoice <=3) {
            status = statusChoice;
        }
        // 调用业务层查询
        List<RepairOrder> orderList = orderService.selectOrder (status);
        if (orderList == null || orderList.isEmpty()) {
            System.out.println("暂无报修单！");
            return;
        }
        // 遍历展示
        for (int i = 0; i < orderList.size(); i++) {
            RepairOrder order = orderList.get(i);
            System.out.println((i+1) + "、编号：" + order.getOrderId()
                    + " | 学生：" + order.getStudentAccount()
                    + " | 宿舍：" + order.getBuilding() + "-" + order.getRoomNum()
                    + " | 状态：" + getStatusDesc(order.getStatus()));
        }
    }

    // 管理员更新报修单状态
    private static void updateOrderStatus() {
        System.out.println("\n===== 更新报修单状态 =====");
        System.out.print("请输入要更新的报修单编号：");
        String orderId = sc.next().trim();
        System.out.println("请选择新状态：");
        System.out.println("0-待处理  1-处理中  2-已完成");
        int status = getIntInput("请选择（输入0-2）：");
        if (orderService.updateOrderStatus(orderId, status)) {
            System.out.println("报修单状态更新成功！新状态：" + getStatusDesc(status));
        } else {
            System.out.println("报修单状态更新失败！");
        }
    }

    //管理员删除报修单
    private static void deleteOrder() {
        System.out.println("\n===== 删除报修单 =====");
        System.out.print("请输入要删除的报修单编号：");
        String orderId = sc.next().trim();
        int choice = getIntInput("确认删除？（1-确认，0-取消）：");
        if (choice == 1) {
            if (orderService.deleteOrder(orderId)) {
                System.out.println("报修单删除成功！");
            } else {
                System.out.println("报修单删除失败！");
            }
        } else {
            System.out.println("已取消操作！");
        }
    }

    //通用：修改密码
    private static void updatePassword() {
        System.out.println("\n===== 修改密码 =====");
        System.out.print("请输入原密码：");
        String oldPwd = sc.next().trim();
        System.out.print("请输入新密码：");
        String newPwd = sc.next().trim();
        System.out.print("请确认新密码：");
        String confirmPwd = sc.next().trim();
        if (!newPwd.equals(confirmPwd)) {
            System.out.println("两次新密码输入不一致！");
            return;
        }
        if (userService.updatePassword(currentUser.getAccount(), oldPwd, newPwd)) {
            System.out.println("密码修改成功！请重新登录");
            currentUser = null; // 清空用户，返回主菜单
        } else {
            System.out.println("密码修改失败！原密码错误");
        }
    }
    //通用：获取整数输入（处理非数字输入）
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int num = sc.nextInt();
                sc.nextLine(); // 吸收换行符
                return num;
            } else {
                System.out.println("输入错误！请输入数字");
                sc.next(); // 清除非数字输入
            }
        }
    }
}
