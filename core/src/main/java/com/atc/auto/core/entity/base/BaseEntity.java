package com.atc.auto.core.entity.base;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * BaseEntity - 实体基类
 * 持久化监听器{@link }
 * 自增主键{@link #getId()} 创建时间{@link #getCreateDate()}
 * 修改时间{@link #getModifyDate()} 版本{@link #getVersion()}
 * 判断对象是否为新建对象{@link #isNew()}
 *
 * @author Lijin
 * @version 1.0.0
 */
/*@EntityListeners(EntityListener.class)*/
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -4339283425096400631L;

    public static final String CREATE_DATE_PROPERTY_NAME = "createDate";

    public static final String MODIFY_DATE_PROPERTY_NAME = "modifyDate";

    public static final String VERSION_PROPERTY_NAME = "version";

    /** 主键 */
    private ID id;
    /** 创建时间 */
    private Date createDate;
    /** 修改时间 */
    private Date modifyDate;
    /** 版本 */
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Column(nullable = false, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(nullable = false)
    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Version
    @Column(nullable = false)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Transient
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity<?> that = (BaseEntity<?>) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(createDate, that.createDate)
                .append(modifyDate, that.modifyDate)
                .append(version, that.version)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(createDate)
                .append(modifyDate)
                .append(version)
                .toHashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseEntity{");
        sb.append("id=").append(id);
        sb.append(", createDate=").append(createDate);
        sb.append(", modifyDate=").append(modifyDate);
        sb.append(", version=").append(version);
        sb.append('}');
        return sb.toString();
    }
}