package com.atc.auto.core.pojo.authority;

import com.atc.auto.core.entity.authority.Employee;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * EmployeeQuery - 用户查询
 *
 * @author Mengke
 * @version 1.0.0
 */
@Getter
@Setter
public class EmployeeQuery extends Employee {

    private static final long serialVersionUID = 3491815708714358313L;
    /**
     * 机构ids
     */
    private List<Long> institutionIds;
    /**
     * 机构id
     */
    private Long institutionId;
    /**
     * 角色id
     */
    private Long positionId;
    /**
     * 员工集合id
     */
    private List<Long> employeeIds;
}