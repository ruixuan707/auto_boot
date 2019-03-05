package com.atc.auto.core.service.authority;

import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.pojo.authority.InstitutionQuery;
import com.atc.auto.core.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * InstitutionService   机构服务层
 *
 * @author Monco
 * @version 1.0.0
 */
public interface InstitutionService extends BaseService<Institution, Long> {

    /**
     * 列表分页查询
     *
     * @param pageable
     * @param institutionQuery
     * @return
     */
    Page<Institution> getInstitution(Pageable pageable, InstitutionQuery institutionQuery);

    /**
     * 获得所有父级
     *
     * @param id
     * @return
     */
    List<Institution> findParent(Long id);

    /**
     * 获得所有子级
     *
     * @param id
     * @return
     */
    List<Institution> findChildren(Long id);


    /**
     * 删除权限
     *
     * @param institutionIds
     * @param popedomIds
     */
    void deleteInstitutionPopedom(List<Long> institutionIds, List<Long> popedomIds);

    /**
     * 获得部门名称
     *
     * @param institution
     * @return
     */
    String getInstitutionName(Institution institution);

    /**
     * 查找关联的部门
     *
     * @return
     */
    List<Institution> findRelaed();

    /**
     * 按照父级id查找部门
     *
     * @param parentId
     * @return
     */
    List<Institution> findInstitution(Long parentId);

    /**
     * 获得所有子集id
     *
     * @param id
     * @return
     */
    List<Long> findChildrenId(Long id);


    List<Institution> findChildrenInstitution();
}
