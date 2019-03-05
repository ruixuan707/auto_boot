package com.atc.auto.core.impl.authority;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.atc.auto.core.dao.authority.EmployeeDao;
import com.atc.auto.core.entity.authority.Employee;
import com.atc.auto.core.impl.base.BaseServiceImpl;
import com.atc.auto.core.pojo.authority.EmployeeQuery;
import com.atc.auto.core.service.authority.EmployeeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * UserServiceImpl - 用户业务层
 *
 * @author Lijin
 * @version 1.0.0
 */
@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Long> implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "employee", key = "#username")
    public Employee getEmployeeByUsername(String username) {
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setIsDelete(0);
        Example<Employee> employeeExample = Example.of(employee);
        return employeeDao.findOne(employeeExample).orElse(null);
    }

    @Override
    @Transactional
//    @CacheEvict(value = "employee", key = "#entity.username")
    public Employee save(Employee entity) {
        super.save(entity);
        return getEmployeeByUsername(entity.getUsername());
    }

    @Override
    @Transactional
//    @CacheEvict(value = "employee", key = "#entity.username")
    public Employee update(Employee entity) {
        return super.update(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getList(Pageable pageable, EmployeeQuery employeeQuery) {
        Page<Employee> result = findAll((Specification<Employee>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(employeeQuery.getUsername())) {
                predicateList.add(criteriaBuilder.like(
                        root.get("username").as(String.class),
                        "%" + employeeQuery.getUsername() + "%"));
            }
            if (employeeQuery.getId() != null) {
                predicateList.add(criteriaBuilder.equal(
                        root.get("id").as(Long.class),
                        employeeQuery.getId()));
            }
            if (StringUtils.isNotBlank(employeeQuery.getNickName())) {
                predicateList.add(criteriaBuilder.like(
                        root.get("nickName").as(String.class),
                        "%" + employeeQuery.getNickName() + "%"));
            }
            if (StringUtils.isNotBlank(employeeQuery.getPost())) {
                predicateList.add(criteriaBuilder.like(
                        root.get("post").as(String.class),
                        "%" + employeeQuery.getPost() + "%"));
            }
            if (employeeQuery.getPositionId() != null) {
                predicateList.add(criteriaBuilder.equal(
                        root.get("position").get("id").as(Long.class),
                        employeeQuery.getPositionId()));
            }
            if (!CollectionUtils.isEmpty(employeeQuery.getInstitutionIds())) {
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("institution").get("id"));
                for (Long id : employeeQuery.getInstitutionIds()) {
                    in.value(id);
                }
                predicateList.add(in);
            }
            if (CollectionUtils.isNotEmpty(employeeQuery.getEmployeeIds())) {
                CriteriaBuilder.In<Object> in =
                        criteriaBuilder.in(root.get("id"));
                for (Long id : employeeQuery.getEmployeeIds()) {
                    in.value(id);
                }
                predicateList.add(in);
            }
            predicateList.add(criteriaBuilder.equal(
                    root.get("isDelete").as(Integer.class), 0));
            Predicate[] predicates = new Predicate[predicateList.size()];
            query.where(predicateList.toArray(predicates));
            return query.getRestriction();
        }, pageable);
        return result;
    }

    @Override
    public List<Employee> getEmployees(Long[] institutionIds) {
        return employeeDao.getEmployee(institutionIds);
    }

    @Override
    public List<Employee> getEmployeeByInstitutionId(Long institutionId) {
        return employeeDao.getEmployeeByInstitutionId(institutionId);
    }

    @Override
//    @CacheEvict(value = "employee", allEntries = true)
    public void saveCollection(List<Employee> list) {
        super.saveCollection(list);
    }
}