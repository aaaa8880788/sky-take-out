package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /*
        新增员工
    */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /*
    * 员工分页查询
    *  */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询,参数为:{}", employeePageQueryDTO);
        if(employeePageQueryDTO.getPage() == null) {
            return Result.error("page参数必填");
        }
        if(employeePageQueryDTO.getPageSize() == null) {
            return Result.error("pageSize参数必填");
        }
        PageResult pageResult =  employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /*
    * 启用禁用员工账号
    *  */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result<String> updateStatus(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号,参数为 status:{},id:{}", status, id);
        if(status == null) {
            return Result.error("status参数必填");
        }
        if(id == null) {
            return Result.error("id参数必填");
        }
        employeeService.updateStatus(status, id);
        return Result.success("编辑成功");
    }

    /*
    * 根据id查询员工信息
    *  */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> getById(@PathVariable Long id) {
      Employee employee = employeeService.getById(id);
      return Result.success(employee);
    }

    /*
    * 编辑员工信息
    *  */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success("编辑成功");
    }
}
