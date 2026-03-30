package com.dorm.newrepair.controller;

import com.dorm.newrepair.model.User;
import com.dorm.newrepair.service.UserService;
import com.dorm.newrepair.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //这个没啥用 留着测试
    @GetMapping("/User")
    public String getUser() {
        return "User data here";
    }


    @PostMapping("/login")
    public Result<User> login(@RequestParam String account,
                              @RequestParam String password) {
        User result = userService.login(account, password);
        return Result.success(result);
    }

    @PostMapping
    public Result<Boolean> register(@RequestBody User user) {
        boolean result = userService.register(user);
        return Result.success(result);
    }

    @PatchMapping("/{account}/Password")
    public Result<Boolean> updatePassword(
            @PathVariable String account,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        boolean result = userService.updatePassword(account, oldPassword, newPassword);
        return Result.success(result);
    }

    @PatchMapping("/{account}/Dorm")
    public Result<Boolean> updateDorm(
            @PathVariable String account,
            @RequestParam String building,
            @RequestParam String roomNum) {
        boolean result = userService.updateDorm(account, building, roomNum);
        return Result.success(result);
    }
}
