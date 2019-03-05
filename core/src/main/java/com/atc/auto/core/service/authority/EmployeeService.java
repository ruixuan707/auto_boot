package com.atc.auto.core.service.authority;

import com.atc.auto.core.entity.authority.Employee;
import com.atc.auto.core.pojo.authority.EmployeeQuery;
import com.atc.auto.core.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * UserService - 用户业务层接口
 *
 * @author Lijin
 * @version 1.0.0
 */
public interface EmployeeService extends BaseService<Employee, Long> {

    /**
     * 根据用户名查找用户
     *
     * @param username
     *         用户名
     * @return 用户
     */
    Employee getEmployeeByUsername(String username);

    /**
     * 分页查询用户
     *
     * @param pageable
     *         分页
     * @param employeeQuery
     *         查询条件
     * @return 用户
     */
    Page<Employee> getList(Pageable pageable, EmployeeQuery employeeQuery);


    /**
     * 通过institutionIds查找人
     *
     * @param institutionIds
     *         机构
     * @return Employee
     */
    List<Employee> getEmployees(Long[] institutionIds);


    /**
     * 通过部门id查找员工
     *
     * @param institutionId
     *         institutionIds
     * @return Employee
     */
    List<Employee> getEmployeeByInstitutionId(Long institutionId);


}