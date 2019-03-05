package com.atc.auto.core.dao.base;

import com.atc.auto.core.entity.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * JPA dao层基类
 *
 * @param <T>
 * @param <ID>
 */
@Repository
public interface BaseDao<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}