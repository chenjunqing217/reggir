package com.it.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.reggie.common.R;
import com.it.reggie.entity.Employee;
import com.it.reggie.mapper.EmployeeMapper;
import com.it.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /**
     * 用户登录
     * @param employee
     * @param request
     * @return
     */
    @Override
    public R login(Employee employee, HttpServletRequest request) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = this.getOne(queryWrapper);
        if (emp == null){
            return R.error("用户名错误！");
        }
        log.info("=====11:"+emp.getPassword());
        log.info("=====22:"+employee.getPassword());
        if (emp.getPassword().equals(employee.getPassword())){
            return R.error("密码错误");
        }
        if (emp.getStatus()!=1){
            return R.error("权限不足！");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @Override
    public R logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功。。。");
    }

    /**
     * 新增用户
     *
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R saveemployee(HttpServletRequest request, Employee employee) {
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(id);
        boolean save = this.save(employee);
        if (save){
            return R.success("成功");
        }else{
            return R.error("失败");
        }
    }

    /**
     * 分页查询
     * @param page
     * @param pagesize
     * @param name
     * @return
     */
    @Override
    public R<Page> pageSelect(int page, int pagesize, String name) {
        //构造分页
        Page pageinfo = new Page(page,pagesize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getCreateTime);
        this.page(pageinfo,queryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 员工状态，启用，禁用
     * @param employee
     * @return
     */
    @Override
    public R updateOrDelete(Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
        employee.setId(employee.getId());
        this.updateById(employee);
        return R.success("成功了！");
    }

    /**
     * 员工信息展示
     * @param id
     * @return
     */
    @Override
    public R<Employee> getdateById(Long id) {
        Employee employee = this.getById(id);
        return R.success(employee);
    }
}
