package com.sky.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {
    // 员工姓名
    private String name;
    // 页码
    private Integer page;
    // 每页显示记录数
    private Integer pageSize;
}



