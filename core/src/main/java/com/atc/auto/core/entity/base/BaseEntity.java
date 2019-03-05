package com.atc.auto.core.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.atc.auto.core.listener.EntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * BaseEntity - 实体基类
 *
 * @author Lijin
 * @version 1.0.0
 */
@MappedSuperclass
@EntityListeners(EntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 5319472904906621525L;

    /**
     * 主键
     */
    private ID id;
    /**
     * 版本
     */
    private Long version;
    /**
     * 删除标志位  0:正常 1:删除
     */
    private Integer isDelete = 0;
    /**
     * 创建人ID
     */
    private Long createdId;
    /**
     * 创建人
     */
    private String createdName;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人ID
     */
    private Long updatedId;
    /**
     * 修改人
     */
    private String updatedName;
    /**
     * 修改时间
     */
    private Date updateDate;

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

    @Version
    @Column(nullable = false)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Column(nullable = false)
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Column(nullable = false)
    public Long getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Long createdId) {
        this.createdId = createdId;
    }

    @Column(nullable = false)
    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public Long getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(Long updatedId) {
        this.updatedId = updatedId;
    }

    public String getUpdatedName() {
        return updatedName;
    }

    public void setUpdatedName(String updatedName) {
        this.updatedName = updatedName;
    }

    @Column(nullable = false)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Transient
    @JSONField(serialize = false)
    public boolean isNew() {
        return getId() == null;
    }

}