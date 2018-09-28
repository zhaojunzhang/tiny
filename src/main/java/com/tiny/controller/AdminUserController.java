package com.tiny.controller;

import com.tiny.entity.AdminUser;
import com.tiny.service.AdminUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by zhangzhaojun on 2018/9/28.
 */
@RestController
@RequestMapping(value="/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public Object adminLogin(String username,String password){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return null;
        }
        //todo 密码加密 校验
        AdminUser adminUser = adminUserService.queryAdminUser(username);
        String passwordEncrypt = password;
        if(!StringUtils.equalsIgnoreCase(adminUser.getPassword(),passwordEncrypt)){
            return null;
        }
        return "success";
    }

}
