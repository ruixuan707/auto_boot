package com.atc.auto.core.service.authority;

import com.atc.auto.core.entity.authority.Position;
import com.atc.auto.core.pojo.authority.PositionQuery;
import com.atc.auto.core.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * PositionService
 *
 * @author Monco
 * @version 1.0.0
 */
public interface PositionService extends BaseService<Position, Long> {

    /**
     * 获得岗位分页列表
     *
     * @param pageable
     * @param positionQuery
     * @return
     */
    Page<Position> getPosition(Pageable pageable, PositionQuery positionQuery);

    /**
     * 按照机构下获得所有岗位
     *
     * @param institutionIds
     * @return
     */
    List<Position> getPosition(List<Long> institutionIds);

    /**
     * 按照机构下获得所有岗位
     *
     * @param institutionId
     * @return
     */
    List<Position> getPosition(Long institutionId);

    /**
     * 按照机构获得所有岗位
     *
     * @return
     */
    List<Position> getPosition();

    /**
     * 删除角色权限
     *
     * @param positionIds
     * @param popedomIds
     */
    void deletePositionPopedom(List<Long> positionIds, List<Long> popedomIds);

}
