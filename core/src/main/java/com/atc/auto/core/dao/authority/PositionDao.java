package com.atc.auto.core.dao.authority;

import com.atc.auto.core.dao.base.BaseDao;
import com.atc.auto.core.entity.authority.Position;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PositionDao
 *
 * @author Monco
 * @version 1.0.0
 */
public interface PositionDao extends BaseDao<Position, Long> {

    @Query(value = "select * from base_position p where p.institution_id in(?1) and p.is_delete = 0", nativeQuery = true)
    List<Position> getPosition(List<Long> institutionIds);

    @Query(value = "select * from base_position p where p.institution_id = ?1 and p.is_delete = 0", nativeQuery = true)
    List<Position> getPosition(Long institutionId);

    @Query(value = "select * from base_position p where p.is_delete = 0", nativeQuery = true)
    List<Position> getPosition();

    @Modifying
    @Transactional
    @Query(value = "delete from base_position_popedom where position_id in (?1) and popedom_id in (?2)", nativeQuery = true)
    void deletePositionPopedom(List<Long> positionIds, List<Long> PopedomIds);
}
