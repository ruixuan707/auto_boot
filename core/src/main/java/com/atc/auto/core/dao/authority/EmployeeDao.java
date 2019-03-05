package com.atc.auto.core.dao.authority;

import com.atc.auto.core.dao.base.BaseDao;
import com.atc.auto.core.entity.authority.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserDao - 用户持久层
 *
 * @author Lijin
 * @version 1.0.0
 */
@Repository
public interface EmployeeDao extends BaseDao<Employee, Long> {

    /**
     * 通过部门集合查询用户
     *
     * @param institutionIds
     * @return
     */
    @Query(value = "select * from base_employee u where u.institution_id in (?1) and u.is_delete = 0", nativeQuery = true)
    List<Employee> getEmployee(Long[] institutionIds);

    /**
     * 通过部门id查找员工
     *
     * @param institutionId
     * @return
     */
    @Query(value = "select * from base_employee u where u.institution_id = (?1) and u.is_delete = 0", nativeQuery = true)
    List<Employee> getEmployeeByInstitutionId(Long institutionId);

}

