package com.atc.auto.core.impl.authority;

import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.core.dao.authority.PositionTemplateDao;
import com.atc.auto.core.entity.authority.PositionTemplate;
import com.atc.auto.core.impl.base.BaseServiceImpl;
import com.atc.auto.core.pojo.authority.PositionTemplateQuery;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.core.service.authority.PositionTemplateService;
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
 * PositionTemplateServiceImpl
 *
 * @author Monco
 * @version 1.0.0
 */
@Service
public class PositionTemplateServiceImpl extends BaseServiceImpl<PositionTemplate, Long> implements PositionTemplateService {

    @Autowired
    private PositionTemplateDao positionTemplateDao;

    @Autowired
    private PopedomService popedomService;

    @Override
    public Page<PositionTemplate> getPositionTemplate(Pageable pageable, PositionTemplateQuery positionTemplateQuery) {

        Page<PositionTemplate> result = positionTemplateDao.findAll(new Specification<PositionTemplate>() {
            @Override
            public Predicate toPredicate(Root<PositionTemplate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
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
    public List<PositionTemplate> getPositionTemplate(Long institutionId) {
        return positionTemplateDao.getPositionTemplate(institutionId);
    }

    @Override
    public List<PositionTemplate> getPositionTemplate() {
        return positionTemplateDao.getPositionTemplate();
    }

}