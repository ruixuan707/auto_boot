package com.atc.auto.core.dao.authority;

import com.atc.auto.core.entity.authority.Popedom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PopedomDao - 权限持久层
 *
 * @author Lijin
 * @version 1.0.0
 */
public interface PopedomDao extends JpaRepository<Popedom, Long>, JpaSpecificationExecutor<Popedom> {

}

