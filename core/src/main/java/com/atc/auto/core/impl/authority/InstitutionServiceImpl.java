package com.atc.auto.core.impl.authority;

import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.core.dao.authority.InstitutionDao;
import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.impl.base.BaseServiceImpl;
import com.atc.auto.core.pojo.authority.InstitutionQuery;
import com.atc.auto.core.service.authority.InstitutionService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
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
 * InstitutionServiceImpl   机构实现类
 *
 * @author Monco
 * @version 1.0.0
 */
@Service
public class InstitutionServiceImpl extends BaseServiceImpl<Institution, Long> implements InstitutionService {

    @Autowired
    private InstitutionDao institutionDao;

    @Override
    public Page<Institution> getInstitution(Pageable pageable, InstitutionQuery institutionQuery) {
        Page<Institution> result = institutionDao.findAll(new Specification<Institution>() {
            @Override
            public Predicate toPredicate(Root<Institution> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                //机构名字
                if (StringUtils.isNotBlank(institutionQuery.getInstitutionName())) {
                    predicateList.add(criteriaBuilder.like(
                            root.get("institutionName").as(String.class),
                            "%" + institutionQuery.getInstitutionName() + "%"));
                }
                //子级搜索
                if (institutionQuery.getChildrenIds() != null && institutionQuery.getChildrenIds().size() > 0) {
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("id"));
                    for (Long id : institutionQuery.getChildrenIds()) {
                        in.value(id);
                    }
                    predicateList.add(in);
                }
                predicateList.add(criteriaBuilder.equal(
                        root.get("isDelete").as(Integer.class), 0));
                Predicate[] predicates = new Predicate[predicateList.size()];
                query.where(predicateList.toArray(predicates));
                return query.getRestriction();
            }
        }, pageable);
        return result;
    }

    @Override
    public List<Institution> findParent(Long id) {
        List<Institution> institutionList = new ArrayList<>();
        getParent(id, institutionList);
        return institutionList;
    }

    @Override
    public List<Institution> findChildren(Long id) {
        List<Institution> institutionList = new ArrayList<>();
        getChildren(id, institutionList);
        return institutionList;
    }

    @Override
    public void deleteInstitutionPopedom(List<Long> institutionIds, List<Long> popedomIds) {
        institutionDao.deleteInstitutionPopedom(institutionIds, popedomIds);
    }

    @Override
    public String getInstitutionName(Institution institution) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(institution.getInstitutionPath())) {
            String[] strings = institution.getInstitutionPath().split(",");
            Long[] pathNumber = (Long[]) ConvertUtils.convert(strings, Long.class);
            List<Institution> institutionList = this.findByIds(pathNumber);
            institutionList.add(institution);
            if (CollectionUtils.isNotEmpty(institutionList)) {
                for (int i = 0; i < institutionList.size(); i++) {
                    if (i != 0) {
                        sb.append(" / ");
                    }
                    sb.append(institutionList.get(i).getInstitutionName());
                }
            } else {
                sb.append(PropertyUtil.DEFAULT_NULL);
            }
        } else {
            sb.append(institution.getInstitutionName());
        }
        return sb.toString();
    }

    @Override
    public List<Institution> findRelaed() {
        return institutionDao.findRelaed();
    }

    @Override
    public List<Institution> findInstitution(Long parentId) {
        return institutionDao.findInstitution(parentId);
    }

    @Override
    public List<Long> findChildrenId(Long id) {
        List<Institution> institutionList = new ArrayList<>();
        getChildren(id, institutionList);
        List<Long> institutionIds = new ArrayList<>();
        for (Institution institution : institutionList) {
            institutionIds.add(institution.getId());
        }
        return institutionIds;
    }

    @Override
    public List<Institution> findChildrenInstitution() {
        List<Institution> institutionList = institutionDao.findAll();
        List<Institution> list = new ArrayList<>();
        for (Institution institution : institutionList) {
            if (CollectionUtils.isEmpty(this.findChildren(institution.getId()))) {
                list.add(institution);
            }
        }
        return list;
    }

    public void getParent(Long id, List<Institution> institutions) {
        Institution institution = this.find(id);
        if (institution.getParentId() == 0L) {
            Institution parentInstitution = this.find(institution.getParentId());
            institutions.add(parentInstitution);
            getParent(institution.getId(), institutions);
        }
    }

    public void getChildren(Long id, List<Institution> institutions) {
        List<Institution> institutionList = institutionDao.findByParentIdAndIsDelete(id, 0);
        if (CollectionUtils.isNotEmpty(institutionList)) {
            for (Institution institution : institutionList) {
                institutions.add(institution);
                getChildren(institution.getId(), institutions);
            }
        }
    }
}