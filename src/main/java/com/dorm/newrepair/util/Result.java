package com.dorm.newrepair.util;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    //成功带数据
    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.setCode(200); //返回状态码200 表示成功
        r.setMsg("操作成功！");
        r.setData(data);
        return r;
    }

    //成功返回 无数据
    public static <T> Result <T> success(){
        return success(null);
    }

    //失败返回
    public static <T> Result<T> error(int code,String msg){
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }

    public static <T> Result<T> error(String msg){
        return error(500,msg);//500状态码 表示失败
    }
}
