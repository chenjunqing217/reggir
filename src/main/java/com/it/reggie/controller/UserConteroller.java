package com.it.reggie.controller;

import com.it.reggie.common.R;
import com.it.reggie.entity.User;
import com.it.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserConteroller {

    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsgs(@RequestBody User user, HttpServletRequest request){
        R out = userService.sendMsgs(user,request);
        return out;
    }

    /**
     * 登录
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Map user, HttpSession session){
        R out = userService.login(user,session);
        return out;
    }

}
