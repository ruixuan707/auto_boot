package com.atc.auto.api.authority;

import com.atc.auto.common.bean.ApiResult;
import com.atc.auto.common.response.ResultMessageBuilder;
import com.atc.auto.common.response.ReturnCodeEnum;
import com.atc.auto.common.util.CommonUtils;
import com.atc.auto.common.util.MD5Util;
import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.common.util.UpdateUtil;
import com.atc.auto.core.entity.authority.Employee;
import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.entity.authority.Position;
import com.atc.auto.core.manager.EmployeeManager;
import com.atc.auto.core.mapper.authority.EmployeeMapper;
import com.atc.auto.core.pojo.authority.EmployeeQuery;
import com.atc.auto.core.pojo.base.OrderQuery;
import com.atc.auto.core.service.authority.EmployeeService;
import com.atc.auto.core.service.authority.InstitutionService;
import com.atc.auto.core.service.authority.PositionService;
import com.atc.auto.page.authority.EmployeePage;
import com.atc.auto.page.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeController
 *
 * @author Mengke
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @PostMapping
//    @RequiresPermissions("employee:save")
    public ResponseEntity<String> save(@RequestBody EmployeePage employeePage) {
        Employee entity = employeeService.getEmployeeByUsername(employeePage.getUsername());
        Employee employee = new Employee();
        pageToEntity(employeePage, employee);
        employee.setPassword(MD5Util.encrypt(employeePage.getPassword()));
        employeeService.save(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
//    @RequiresPermissions("employee:query")
    public ResponseEntity<PageResult> getList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", defaultValue = "20") Integer size,
                                              EmployeeQuery employeeQuery, OrderQuery orderQuery) {
        if (employeeQuery.getInstitutionId() != null) {
            List<Long> institutionIds = institutionService.findChildrenId(employeeQuery.getInstitutionId());
            institutionIds.add(employeeQuery.getInstitutionId());
            employeeQuery.setInstitutionIds(institutionIds);
        }
        Page<Employee> pageList = employeeService.getList(OrderQuery.getQuery(orderQuery, page, size), employeeQuery);
        List<Employee> employeeList = pageList.getContent();
        List<EmployeePage> employeePageList = new ArrayList<>();
        for (Employee employee : employeeList) {
            EmployeePage employeePage = new EmployeePage();
            entityToPage(employee, employeePage);
            employeePageList.add(employeePage);
        }
        PageResult pageResult = new PageResult(pageList.getPageable(), employeePageList, pageList.getTotalElements());
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("list")
//    @RequiresPermissions("employee:query")
    public ResponseEntity<List<EmployeePage>> getEmployees(EmployeeQuery employeeQuery) {
        return null;
    }

    @PutMapping
//    @RequiresPermissions("employee:update")
    public ResponseEntity<Void> update(@RequestBody EmployeePage employeePage) {
        Employee employee = new Employee();
        pageToEntity(employeePage, employee);
        Employee source = employeeService.find(employeePage.getId());
        UpdateUtil.copyNonNullProperties(employee, source);
        employeeService.save(source);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
//    @RequiresPermissions("employee:delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Employee employee = employeeService.find(id);
        employee.setIsDelete(1);
        employeeService.save(employee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}")
//    @RequiresPermissions("employee:query")
    public ResponseEntity<EmployeePage> getOne(@PathVariable Long id) {
        EmployeePage employeePage = new EmployeePage();
        Employee employee = employeeService.find(id);
        entityToPage(employee, employeePage);
        return ResponseEntity.ok(employeePage);
    }

    @GetMapping("all")
    public ResponseEntity<List<EmployeePage>> getList(EmployeeQuery query) {
        List<Employee> employees = new ArrayList<>();
        List<EmployeePage> employeePageList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        if (query.getInstitutionId() != null) {
            List<Institution> institutionList = institutionService.findChildren(query.getInstitutionId());
            //是否有子集
            if (CollectionUtils.isNotEmpty(institutionList)) {
                institutionList.add(institutionService.find(query.getInstitutionId()));
                for (Institution institution : institutionList) {
                    ids.add(institution.getId());
                }
                //通过部门id和部门下属子集id查询用户
                employees = employeeService.getEmployees(CommonUtils.list2Array(ids));
            } else {
                //通过部门id查询用户
                employees = employeeService.getEmployeeByInstitutionId(query.getInstitutionId());
            }
        } else {
            Employee entity = new Employee();
            BeanUtils.copyProperties(query, entity);
            entity.setIsDelete(PropertyUtil.NUM_0);
            Example<Employee> example = Example.of(entity);
            employees = employeeService.findAll(example, Sort.by("id"));
        }
        if (CollectionUtils.isNotEmpty(employees)) {
            for (Employee employee : employees) {
                EmployeePage employeePage = new EmployeePage();
                entityToPage(employee, employeePage);
                employeePageList.add(employeePage);
            }
        }
        return ResponseEntity.ok(employeePageList);
    }

    /**
     * 重置某个用户密码
     *
     * @return
     */
    @PutMapping("reset/password")
    public ResponseEntity<Void> resetPassword(@RequestBody EmployeePage employeePage) {
        try {
            Employee employee = employeeService.find(employeePage.getId());
            for (long i : employeePage.getBatchIds()) {
                if (i == 0L) {
                    employee.setPassword(MD5Util.encryptKL(PropertyUtil.PASS_WORD));
                } else if (i == 1L) {
                    employee.setCheckPassword(null);
                }
            }
            employeeService.update(employee);
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error("更新用户出错:{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 初始化校验密码和登录密码
     *
     * @param employeePage
     * @return
     */
    @PutMapping("update/check")
    public ResponseEntity<String> updateCheckPassword(@RequestBody EmployeePage employeePage) {

        try {
            Employee employee = EmployeeManager.get();
            if (StringUtils.isNoneBlank(employeePage.getCheckPassword()) && StringUtils.isNoneBlank(employeePage.getPassword()) && employee != null) {
                if (employeePage.getCheckPassword().equals(employeePage.getPassword())) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM04);
                }
                String decryptPass = MD5Util.md5Password(employeePage.getPassword());
                if (decryptPass.equals(PropertyUtil.PASS_WORD)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM12);
                }
                employee.setCheckPassword(MD5Util.encrypt(employeePage.getCheckPassword()));
                employee.setPassword(MD5Util.encrypt(employeePage.getPassword()));
                employeeService.update(employee);
            } else {
                return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM13);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            log.error("更新用户出错:{}", e.getMessage());
            return ResultMessageBuilder.buildResponse(ReturnCodeEnum._500);
        }
    }

    /**
     * 初始化校验密码
     *
     * @param employeePage
     * @return
     */
    @PutMapping("update/init/check")
    public ResponseEntity<String> updateInitCheck(@RequestBody EmployeePage employeePage) {

        try {
            Employee employee = EmployeeManager.get();
            if (StringUtils.isNoneBlank(employeePage.getCheckPassword()) && employee != null) {

                String encryptPass = MD5Util.decryptJM(employee.getPassword());
                String encryptCheck = MD5Util.md5Password(employeePage.getCheckPassword());
                if (encryptPass.equals(encryptCheck)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM04);
                }
                employee.setCheckPassword(MD5Util.encrypt(employeePage.getCheckPassword()));
                employeeService.update(employee);
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            } else {
                return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM13);
            }
        } catch (Exception e) {
            log.error("更新用户出错:{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 初始化登录密码
     *
     * @param employeePage
     * @return
     */
    @PutMapping("update/init/pass")
    public ResponseEntity<String> updateInitPass(@RequestBody EmployeePage employeePage) {

        try {
            Employee employee = EmployeeManager.get();
            if (StringUtils.isNoneBlank(employeePage.getPassword()) && employee != null) {

                String encryptCheckPass = MD5Util.decryptJM(employee.getCheckPassword());
                String encryptPass = MD5Util.md5Password(employeePage.getPassword());
                if (encryptCheckPass.equals(encryptPass)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM04);
                }
                employee.setPassword(MD5Util.encrypt(employeePage.getPassword()));
                employeeService.update(employee);
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            } else {
                return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM13);
            }
        } catch (Exception e) {
            log.error("更新用户出错:{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 修改登录密码
     *
     * @param employeePage
     * @return
     */
    @PutMapping("update/pass")
    public ResponseEntity<String> updatePass(@RequestBody EmployeePage employeePage) {

        try {
            Employee employee = EmployeeManager.get();
            if (StringUtils.isNoneBlank(employeePage.getPassword()) && StringUtils.isNoneBlank(employeePage.getOldPassword()) && employee != null) {
                //页面获取密码加密
                String decryptPass = MD5Util.md5Password(employeePage.getPassword());
                String decryptOldPass = MD5Util.md5Password(employeePage.getOldPassword());
                //数据库获取密码解密
                String encryptPass = MD5Util.decryptJM(employee.getPassword());
                String encryptCheckPass = MD5Util.decryptJM(employee.getCheckPassword());
                if (decryptPass.equals(PropertyUtil.PASS_WORD)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM12);
                }
                if (!encryptPass.equals(decryptOldPass)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM05);
                }
                if (decryptPass.equals(encryptCheckPass)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM04);
                }
                employee.setPassword(MD5Util.encrypt(employeePage.getPassword()));
                employeeService.update(employee);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            log.error("更新用户出错:{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 修改校验密码
     *
     * @param employeePage
     * @return
     */
    @PutMapping("update/check/pass")
    public ResponseEntity<String> updateCheckPass(@RequestBody EmployeePage employeePage) {

        try {
            Employee employee = EmployeeManager.get();
            if (StringUtils.isNoneBlank(employeePage.getCheckPassword()) && StringUtils.isNoneBlank(employeePage.getOldPassword()) && employee != null) {
                //页面获取密码加密
                String decryptPass = MD5Util.md5Password(employeePage.getCheckPassword());
                String decryptOldPass = MD5Util.md5Password(employeePage.getOldPassword());
                //数据库获取密码解密
                String encryptCheckPass = MD5Util.decryptJM(employee.getCheckPassword());
                String encryptPass = MD5Util.decryptJM(employee.getPassword());
                if (!encryptCheckPass.equals(decryptOldPass)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM05);
                }
                if (decryptPass.equals(encryptPass)) {
                    return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM04);
                }
                employee.setPassword(MD5Util.encrypt(employeePage.getCheckPassword()));
                employeeService.update(employee);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            log.error("更新用户出错:{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 校验密码登录
     *
     * @param employeePage
     * @return
     */
    @PostMapping("match-check-password")
    public ResponseEntity<String> matchCheckPassword(@RequestBody EmployeePage employeePage) {
        Employee employee = employeeService.getEmployeeByUsername(employeePage.getUsername());
        if (employee != null && StringUtils.isNoneBlank(employeePage.getCheckPassword())) {
            String encryptCheckPass = MD5Util.decryptJM(employee.getCheckPassword());
            String decryptCheckPass = MD5Util.md5Password(employeePage.getCheckPassword());
            if (encryptCheckPass.equals(decryptCheckPass)) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM11);
            }
        }
        return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM08);
    }

    /**
     * 校验用户名
     *
     * @param employeePage
     * @return
     */
    @GetMapping("match")
    public ResponseEntity<String> matchUsername(EmployeePage employeePage) {

        Employee employee = employeeService.getEmployeeByUsername(employeePage.getUsername());
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResultMessageBuilder.buildResponse(ReturnCodeEnum._FM10);
    }

    /**
     * 实体转页面对象
     *
     * @param employee
     * @param employeePage
     */
    public void entityToPage(Employee employee, EmployeePage employeePage) {
        BeanUtils.copyProperties(employee, employeePage);
        employeePage.setId(employee.getId());
        if (employee.getPosition() != null) {
            //角色信息
            employeePage.setPositionName(employee.getPosition().getPositionName());
            employeePage.setPositionId(employee.getPosition().getId());
            //部门信息
            Institution institution = employee.getPosition().getInstitution();
            if (institution != null) {
                employeePage.setInstitutionName(employee.getInstitutionName());
            } else {
                employeePage.setInstitutionName(PropertyUtil.DEFAULT_NULL);
            }
        }
    }

    /**
     * 页面对象转化
     *
     * @param employeePage
     * @param employee
     */
    public void pageToEntity(EmployeePage employeePage, Employee employee) {
        BeanUtils.copyProperties(employeePage, employee);
        employee.setId(employeePage.getId());
        if (employeePage.getPositionId() != null) {
            employee.setPosition(positionService.find(employeePage.getPositionId()));
            Position position = positionService.find(employeePage.getPositionId());
            Institution institution = position.getInstitution();
            employee.setInstitution(institution);
            if (institution != null) {
                employee.setInstitutionName(institutionService.getInstitutionName(institution));
            } else {
                employee.setInstitutionName(PropertyUtil.DEFAULT_NULL);
            }
        }
    }

    @GetMapping("mybatis")
    public ApiResult getEmployee(@RequestParam Long id) {
        ApiResult apiResult = new ApiResult();
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        if (employee == null) {
            apiResult.setMsg("用户不存在");
            apiResult.setStatus(504);
            apiResult.setData(null);
        } else {
            apiResult.setData(employee);
            apiResult.setData(employee);
        }
        return apiResult;
    }
}