package com.atc.auto.core.entity.authority;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Popedom - 权限表
 *
 * @author Lijin
 * @version 1.0.0
 */
@Entity
@Table(name = "base_popedom")
public class Popedom implements Serializable {

    private static final long serialVersionUID = -5701771598419142557L;
    /** 主键 */
    private Long id;
    /** 父级权限ID */
    private Long parentId;
    /** 菜单权限字段名称 */
    private String name;
    /** 菜单权限字段代码 */
    private String code;
    /** 菜单权限字段代码层级路径 */
    private String codePath;
    /** 权限类型 （1菜单2按钮3字段） */
    private Integer type;
    /** 排序 */
    private Integer sequence;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /** 子级权限 */
    private List<Popedom> children;

    /** 0为未匹配 1为匹配 */
    private Integer matching = 0;

    @Transient
    public List<Popedom> getChildren() {
        return children;
    }

    public void setChildren(List<Popedom> children) {
        this.children = children;
    }

    @Transient
    public Integer getMatching() {
        return matching;
    }

    public void setMatching(Integer matching) {
        this.matching = matching;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Popedom popedom = (Popedom) o;

        return new EqualsBuilder()
                .append(parentId, popedom.parentId)
                .append(name, popedom.name)
                .append(code, popedom.code)
                .append(codePath, popedom.codePath)
                .append(type, popedom.type)
                .append(sequence, popedom.sequence)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(parentId)
                .append(name)
                .append(code)
                .append(codePath)
                .append(type)
                .append(sequence)
                .toHashCode();
    }
}
