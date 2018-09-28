package com.tiny.dao;

import java.util.List;

import com.tiny.dto.input.AdminUserQueryCondition;
import com.tiny.entity.AdminUser;
import com.tiny.entity.AdminUserExample;
import com.tiny.mapper.AdminUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: PeterPuff
 * @date : 2018-09-27 下午8:13
 */

@Repository
public class AdminUserDao {

    @Autowired
    private AdminUserMapper adminUserMapper;

    public AdminUser getInfomationById(int id){
        adminUserMapper.selectByExample(new AdminUserExample());
        return adminUserMapper.selectByPrimaryKey(id);
    }


    public List<AdminUser> queryByCondition(AdminUserQueryCondition condition){
        AdminUserExample example = buildAdminUserExample(condition);
       return adminUserMapper.selectByExample(example);
    }

    private AdminUserExample buildAdminUserExample(AdminUserQueryCondition condition){
        AdminUserExample example = new AdminUserExample();
        AdminUserExample.Criteria criteria = example.createCriteria();
        Integer id = condition.getId();
        String username = condition.getUserName();
        String password = condition.getPassword();

        if(id != null){
            criteria.andIdEqualTo(id);
        }
        if(StringUtils.isNotBlank(condition.getUserName())){
            criteria.andUserNameEqualTo(username);
        }
        if(StringUtils.isNotBlank(password)){
            criteria.andPasswordEqualTo(password);
        }
        return example;
    }






}
