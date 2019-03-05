package com.atc.auto.core.mapper.authority;


import com.atc.auto.core.entity.authority.Employee;

/**
 * Employee Mapper文件
 */
public interface EmployeeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);
}