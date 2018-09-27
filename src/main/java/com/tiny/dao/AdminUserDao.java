package com.tiny.dao;

import com.tiny.entity.AdminUser;
import com.tiny.entity.AdminUserExample;
import com.tiny.mapper.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: PeterPuff
 * @date : 2018-09-27 下午8:13
 */

@Repository
public class AdminUserDao {

    @Autowired
    private AdminUserMapper adminUser1Mapper;

    public AdminUser getInfomationById(int id){
        adminUser1Mapper.selectByExample(new AdminUserExample());
        return adminUser1Mapper.selectByPrimaryKey(id);
    }





}
