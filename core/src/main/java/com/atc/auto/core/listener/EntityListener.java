package com.atc.auto.core.listener;

import com.atc.auto.core.entity.base.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * EntityListener - 创建日期、修改日期、版本处理
 *
 * @author Monco
 * @version 1.0.0
 */
public class EntityListener {

    /**
     * 实体保存前数据初始化
     *
     * @param entity
     */
    @PrePersist
    public void prePersist(BaseEntity<?> entity) {

    }

    /**
     * 实体更新前数据操作
     *
     * @param entity
     */
    @PreUpdate
    public void preUpdate(BaseEntity<?> entity) {

    }
}