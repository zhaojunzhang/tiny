package com.tiny.dto.input;

import lombok.Data;

/**
 * @author by zhangzhaojun on 2018/9/28.
 */

@Data
public class AdminUserQueryCondition {
    private Integer id;
    private String userName;
    private String password;
}
