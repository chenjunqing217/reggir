package com.it.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.reggie.common.R;
import com.it.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;


public interface EmployeeService extends IService<Employee> {
    R login(Employee employee, HttpServletRequest request);

    R logout(HttpServletRequest request);

    R saveemployee(HttpServletRequest request, Employee employee);

    R pageSelect(int page, int pagesize, String name);

    R updateOrDelete(Employee employee);

    R getdateById(Long id);
}
