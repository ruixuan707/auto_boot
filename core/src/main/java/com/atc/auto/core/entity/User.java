package com.atc.auto.core.entity;

import com.atc.auto.core.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName User
 * @Description TODO
 * @Author monco
 * @Date 2019/3/5 2:22
 * @Version 1.0
 **/
@Data
@Entity
@Table(name = "user")
public class User extends BaseEntity<Long> {

    private static final long serialVersionUID = 6217708285406554678L;

    private String username;
}
