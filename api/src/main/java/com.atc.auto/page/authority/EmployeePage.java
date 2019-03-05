package com.atc.auto.page.authority;

import com.atc.auto.page.base.BasePage;
import lombok.Data;
/**
 * EmployeePage 员工的页面对象
 *
 * @author Mengke
 * @version 1.0.0
 */
@Data
public class EmployeePage extends BasePage {

    private static final long serialVersionUID = -960861762332427948L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 角色名字
     */
    private String positionName;
    private Long positionId;

    /**
     * token
     */
    private String token;
    /**
     * 岗位
     */
    private String post;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 密码
     */
    private String password;
    /**
     * 校验密码
     */
    private String checkPassword;
    /**
     * 部门名称
     */
    private String institutionName;
    /**
     * 树形结构
     */
    private Long key;
    private String label;
    /**
     * 部门id
     */
    private Long institutionId;
    /**
     * 是否关联（卡规则显示）
     */
    private Integer isRelated;

    private String departmentName;
    private String employeePostName;
}