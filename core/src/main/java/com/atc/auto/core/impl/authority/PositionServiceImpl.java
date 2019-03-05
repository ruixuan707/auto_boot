package com.atc.auto.core.impl.authority;

import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.core.dao.authority.PositionDao;
import com.atc.auto.core.entity.authority.Position;
import com.atc.auto.core.impl.base.BaseServiceImpl;
import com.atc.auto.core.pojo.authority.PositionQuery;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.core.service.authority.PositionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * PositionServiceImpl
 *
 * @author Monco
 * @version 1.0.0
 */
@Service
public class PositionServiceImpl extends BaseServiceImpl<Position, Long> implements PositionService {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private PopedomService popedomService;

    @Override
    public Page<Position> getPosition(Pageable pageable, PositionQuery positionQuery) {
        Page<Position> result = positionDao.findAll(new Specification<Position>() {
            @Override
            public Predicate toPredicate(Root<Position> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(positionQuery.getPositionName())) {
                    predicateList.add(criteriaBuilder.like(
                            root.get("positionName").as(String.class),
                            "%" + positionQuery.getPositionName() + "%"));
                }
                predicateList.add(criteriaBuilder.equal(
                        root.get("isDelete").as(Integer.class), PropertyUtil.NUM_0));
                Predicate[] predicates = new Predicate[predicateList.size()];
                query.where(predicateList.toArray(predicates));
                return query.getRestriction();
            }
        }, pageable);
        return result;
    }

    @Override
    public List<Position> getPosition(List<Long> institutionIds) {
        return positionDao.getPosition(institutionIds);
    }

    @Override
    public List<Position> getPosition(Long institutionId) {
        return positionDao.getPosition(institutionId);
    }

    @Override
    public List<Position> getPosition() {
        return positionDao.getPosition();
    }

    @Override
    public void deletePositionPopedom(List<Long>  positionIds, List<Long>  popedomIds) {
        positionDao.deletePositionPopedom(positionIds, popedomIds);
    }
}