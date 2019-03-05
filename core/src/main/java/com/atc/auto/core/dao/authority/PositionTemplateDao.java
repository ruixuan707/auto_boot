package com.atc.auto.core.dao.authority;

import com.atc.auto.core.dao.base.BaseDao;
import com.atc.auto.core.entity.authority.PositionTemplate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * PositionTemplateDao
 *
 * @author Monco
 * @version 1.0.0
 */
public interface PositionTemplateDao extends BaseDao<PositionTemplate, Long> {

    @Query(value = "select * from base_position_template p where p.institution_id = ?1 and p.is_delete = 0", nativeQuery = true)
    List<PositionTemplate> getPositionTemplate(Long institutionId);

    @Query(value = "select * from base_position_template p where p.is_delete = 0", nativeQuery = true)
    List<PositionTemplate> getPositionTemplate();
}
