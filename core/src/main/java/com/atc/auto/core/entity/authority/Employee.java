package com.atc.auto.core.entity.authority;

import com.atc.auto.core.entity.base.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Employee - 用户表
 *
 * @author Lijin
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "base_employee")
public class Employee extends BaseEntity<Long> {

    private static final long serialVersionUID = -1648487403435284515L;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 密码
     */
    private String password;
    /**
     * 校验密码
     */
    private String checkPassword;
    /**
     * 用户名
     */
    private String username;
    /**
     * 岗位
     */
    private String post;
    /**
     * 角色
     */
    private Position position;
    /**
     * 部门
     */
    private Institution institution;
    /**
     * 部门名称
     */
    private String institutionName;
    /**
     * 登录时间
     */
    private Date lastLoginTime;
    /**
     * 登录IP地址
     */
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    public Position getPosition() {
        return position;
    }

    @ManyToOne
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    public Institution getInstitution() {
        return institution;
    }
}