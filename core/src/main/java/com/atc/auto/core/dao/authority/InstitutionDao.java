package com.atc.auto.core.dao.authority;

import com.atc.auto.core.dao.base.BaseDao;
import com.atc.auto.core.entity.authority.Institution;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * InstitutionDao
 *
 * @author Monco
 * @version 1.0.0
 */
public interface InstitutionDao extends BaseDao<Institution, Long> {

    /**
     * 按照父级部门查找子级
     *
     * @param parentId
     * @param isDelete
     * @return
     */
    List<Institution> findByParentIdAndIsDelete(Long parentId, Integer isDelete);

    /**
     * 删除部门权限
     *
     * @param InstitutionIds
     * @param PopedomIds
     */
    @Modifying
    @Transactional
    @Query(value = "delete from base_institution_popedom where institution_id in (?1) and popedom_id in (?2)", nativeQuery = true)
    void deleteInstitutionPopedom(List<Long> InstitutionIds, List<Long> PopedomIds);

    /**
     * 查询关联部门
     *
     * @return
     */

    @Query(value = "select * from base_institution where id in (select DISTINCT(institution_id) from base_category_institution )", nativeQuery = true)
    List<Institution> findRelaed();

    /**
     * 按照父级ID 查询部门
     *
     * @param parentId
     * @return
     */
    @Query(value = "select * from base_institution where parent_id = ?1", nativeQuery = true)
    List<Institution> findInstitution(Long parentId);
}
