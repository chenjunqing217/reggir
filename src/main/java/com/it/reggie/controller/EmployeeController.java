package com.it.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.it.reggie.common.R;
import com.it.reggie.entity.Employee;
import com.it.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 用户登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        R out = employeeService.login(employee,request);
        log.info("outout=="+out.getCode());
        return out;
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<Employee> logout (HttpServletRequest request){
        R out = employeeService.logout(request);
        return out;
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save (HttpServletRequest request,@RequestBody Employee employee){
        log.info("开始新增员工信息：{}",employee.toString());
        R out = employeeService.saveemployee(request,employee);
        return out;
    }

    /**
     * 员工管理列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize, String name){
        log.info("分页查询 page:{} pagesize:{} name:{}",page,pageSize,name);
        R out = employeeService.pageSelect(page,pageSize,name);
        return out;
    }

    /**
     * 员工管理，启动，禁用
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateOrDelete (@RequestBody Employee employee){
        log.info("员工管理，启动，禁用 {}",employee.toString());
        R out = employeeService.updateOrDelete(employee);
        return out;
    }

    /**
     * 查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getdateById (@PathVariable Long id){
        R out = employeeService.getdateById(id);
        return out;
    }

}
