package com.it.reggie.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.BaseContext;
import com.it.reggie.common.R;
import com.it.reggie.entity.AddressBook;
import com.it.reggie.entity.User;
import com.it.reggie.mapper.UserMapper;
import com.it.reggie.service.UserService;
import com.it.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImple extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 发送短信
     * @param user
     * @param request
     * @return
     */
    @Override
    public R sendMsgs(User user, HttpServletRequest request) {
        Integer integer = ValidateCodeUtils.generateValidateCode(4);
        log.info("integer==: "+ integer);

//        调用API发送短信 --省略

        HttpSession session = request.getSession();
        session.setAttribute(user.getPhone(),integer);
        return R.success("OK");

    }

    /**
     * 登录
     * @param user
     * @param session
     * @return
     */
    @Override
    public R login(Map user, HttpSession session) {
        String phone1 = user.get("phone").toString();
        String code = user.get("code").toString();
        String attribute = session.getAttribute(phone1).toString();
        if ((attribute.equals(code))){
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone1);
            User user1 = this.getOne(lambdaQueryWrapper);
//            User user2 = new User();
            if (user1 == null){
                user1 = new User();
                user1.setPhone(phone1);
                user1.setStatus(1);
//                user2.setStatus(1);
                this.save(user1);
            }
            session.setAttribute("user",user1.getId());
//            session.setAttribute("user",user2.getId());
            return R.success("ok");
        }
        return R.error("!ok");
    }

}
