package com.dorm.newrepair.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

    @Data
    public class RepairOrder {
        private String orderId;        // 报修单编号（主键）
        private String studentAccount; // 关联学生账号
        private String building;       // 楼栋
        private String roomNum;        // 房间号（对应数据库room_num）
        private String faultType;      // 故障类型（对应数据库fault_type）
        private String faultDesc;      // 故障描述（对应数据库fault_desc）
        private Integer status;        // 报修状态
        private LocalDateTime createTime;       // 创建时间
        private Date updateTime;       // 最后修改时间
    }

