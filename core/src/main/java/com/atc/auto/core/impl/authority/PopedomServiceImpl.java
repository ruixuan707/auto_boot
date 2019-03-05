package com.atc.auto.core.impl.authority;

import com.atc.auto.core.dao.authority.PopedomDao;
import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.entity.authority.Popedom;
import com.atc.auto.core.entity.authority.Position;
import com.atc.auto.core.entity.authority.PositionTemplate;
import com.atc.auto.core.impl.base.BaseServiceImpl;
import com.atc.auto.core.service.authority.InstitutionService;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.core.service.authority.PositionService;
import com.atc.auto.core.service.authority.PositionTemplateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * PopedomServiceImpl - 权限业务层
 *
 * @author Lijin
 * @version 1.0.0
 */
@Service
public class PopedomServiceImpl extends BaseServiceImpl implements PopedomService {

    @Autowired
    private PopedomDao popedomDao;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionTemplateService templateService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "popedom", key = "'getPopedomByPid_'+#pid")
    public List<Popedom> getPopedomByPid(Long pid) {
        Popedom popedom = new Popedom();
        popedom.setParentId(pid);
        Example<Popedom> example = Example.of(popedom);
        return popedomDao.findAll(example, Sort.by("sequence").ascending());
    }

    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "popedom", key = "'getPopedomByEmployeeId_'+#employeeId")
    public List<Popedom> getPopedomByEmployeeId(Long employeeId) {
        String sql = "SELECT p.id, p.parent_id, p.NAME, p.CODE, p.code_path, p.type FROM fmgt_popedom AS p " +
                "INNER JOIN fmgt_position_popedom ON fmgt_position_popedom.popedom_id = p.id " +
                "INNER JOIN fmgt_position ON fmgt_position.id = fmgt_position_popedom.position_id " +
                "INNER JOIN fmgt_employee ON fmgt_employee.position_id = fmgt_position.id " +
                "WHERE fmgt_employee.id = :employeeId " +
                "AND p.type <> 3 ORDER BY p.sequence ASC; ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("employeeId", employeeId);
        List<Object[]> queryResultList = query.getResultList();
        List<Popedom> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(queryResultList)) {
            for (Object o : queryResultList) {
                Object[] row = (Object[]) o;
                BigInteger id = (BigInteger) row[0];
                BigInteger pid = (BigInteger) row[1];
                String name = (String) row[2];
                String code = (String) row[3];
                String codePath = (String) row[4];
                Integer type = (Integer) row[5];
                Popedom popedom = new Popedom();
                popedom.setId(id.longValue());
                popedom.setParentId(pid.longValue());
                popedom.setName(name);
                popedom.setCode(code);
                popedom.setCodePath(codePath);
                popedom.setType(type);
                result.add(popedom);
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "popedom", key = "'findPopedomById_'+#id")
    public Popedom findPopedomById(Long id) {
        Optional<Popedom> optional = popedomDao.findById(id);
        return optional.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Popedom> getPopedomLazyTree(Long pid, Long positionId, Long type, Long institutionId, Long templateId) {
        List<Popedom> all = new ArrayList<>();
        getChildren(pid, all, type);
        List<List<Popedom>> allList = new ArrayList<>();
        allList.add(all);
        if (institutionId != null) {
            Institution institution = institutionService.find(institutionId);
            if (institution != null) {
                allList.add(new ArrayList<>(institution.getPopedomSet()));
            }
        }
        if (positionId != null) {
            Position position = positionService.find(positionId);
            if (position != null) {
                allList.add(new ArrayList<>(position.getPopedomSet()));
            }
        }
        if (templateId != null) {
            PositionTemplate template = templateService.find(templateId);
            if (template != null) {
                allList.add(new ArrayList<>(template.getPopedomSet()));
            }
        }
        all = allList.stream().reduce((list1, list2) -> {
            list1.retainAll(list2);
            return list1;
        }).orElse(Collections.emptyList());
        return all;
    }

    /**
     * 获得所有子级权限
     *
     * @param id
     *         ID
     * @param list
     *         权限
     */
    public void getChildren(Long id, List<Popedom> list, Long type) {
        List<Popedom> popedomList = this.getPopedomByPid(id);
        if (CollectionUtils.isNotEmpty(popedomList)) {
            for (Popedom popedom : popedomList) {
                list.add(popedom);
                getChildren(popedom.getId(), list, type);
            }
        }
        if (type != null && 1 == type) {
            list.removeIf(popedom -> 1 != popedom.getType());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Popedom> findByIds(Long[] ids) {
        List<Popedom> result = new ArrayList<Popedom>();
        if (ArrayUtils.isNotEmpty(ids)) {
            for (Long id : ids) {
                Popedom popedom = this.findPopedomById(id);
                if (popedom != null) {
                    result.add(popedom);
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    @CacheEvict(value = "popedom", allEntries = true)
    public void saveList(List<Popedom> list) {
        popedomDao.saveAll(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Popedom> queryAll() {
        return popedomDao.findAll();
    }
}