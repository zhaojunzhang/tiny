package com.tiny.service;

import java.util.List;
import com.tiny.dao.AdminUserDao;
import com.tiny.dto.input.AdminUserQueryCondition;
import com.tiny.entity.AdminUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by zhangzhaojun on 2018/9/28.
 */
@Service
public class AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;

    public AdminUser queryAdminUser(String userName){
        AdminUserQueryCondition adminUserQueryCondition = new AdminUserQueryCondition();
        adminUserQueryCondition.setUserName(userName);
        List<AdminUser>  adminUsers =  adminUserDao.queryByCondition(adminUserQueryCondition);
        if(CollectionUtils.isEmpty(adminUsers)){
            return null;
        }
        return adminUsers.get(0);
    }
}
