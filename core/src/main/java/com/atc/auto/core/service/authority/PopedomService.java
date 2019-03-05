package com.atc.auto.core.service.authority;

import com.atc.auto.core.entity.authority.Popedom;

import java.util.List;

/**
 * PopedomService - 权限业务层接口
 *
 * @author Lijin
 * @version 1.0.0
 */
public interface PopedomService {

    /**
     * 根据PID获取权限列表
     *
     * @param pid
     *         PID
     * @return List
     */
    List<Popedom> getPopedomByPid(Long pid);

    /**
     * 根据用户ID获取权限
     *
     * @param employeeId
     *         用户ID
     * @return List
     */
    List<Popedom> getPopedomByEmployeeId(Long employeeId);

    /**
     * 根据ID获取权限
     *
     * @param id
     *         ID
     * @return 权限
     */
    Popedom findPopedomById(Long id);

    /**
     * 按照机构或者角色筛选权限
     *
     * @param pid
     *         pid
     * @param positionId
     *         岗位ID
     * @param type
     *         类型
     * @param institutionId
     *         机构ID
     * @return Popedom
     */
    List<Popedom> getPopedomLazyTree(Long pid, Long positionId, Long type, Long institutionId, Long tempalteId);

    /**
     * 按照id数组查找权限列表
     *
     * @param ids
     *         ID
     * @return Popedom
     */
    List<Popedom> findByIds(Long[] ids);

    /**
     * 保存权限
     *
     * @param list
     *         权限
     */
    void saveList(List<Popedom> list);

    /**
     * 查询所有权限
     *
     * @return 权限
     */
    List<Popedom> queryAll();

}