package com.atc.auto.api.authority;

import com.atc.auto.common.bean.ApiResult;
import com.atc.auto.common.util.CommonUtils;
import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.common.util.TreeToolUtils;
import com.atc.auto.common.util.UpdateUtil;
import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.entity.authority.Popedom;
import com.atc.auto.core.entity.authority.Position;
import com.atc.auto.core.pojo.authority.InstitutionQuery;
import com.atc.auto.core.pojo.base.OrderQuery;
import com.atc.auto.core.service.authority.InstitutionService;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.core.service.authority.PositionService;
import com.atc.auto.page.authority.InstitutionPage;
import com.atc.auto.page.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * InstitutionController
 *
 * @author Monco
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/institution")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private PopedomService popedomService;

    @Autowired
    private PositionService positionService;

    /**
     * 保存机构
     *
     * @param institutionPage
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResult> save(@RequestBody InstitutionPage institutionPage) {
        Institution institution = new Institution();
        BeanUtils.copyProperties(institutionPage, institution);
        if (institutionPage.getParentId() == null) {
            institution.setParentId(0L);
        }
        if (ArrayUtils.isNotEmpty(institutionPage.getInstitutionPath())) {
            institution.setInstitutionPath(CommonUtils.LongArray2String(institutionPage.getInstitutionPath()));
            institution.setInstitutionLevel(institutionPage.getInstitutionPath().length);
        } else {
            institution.setInstitutionLevel(PropertyUtil.NUM_0);
        }
        List<Popedom> popedomList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(institutionPage.getPopedomIds())) {
            popedomList = popedomService.findByIds(institutionPage.getPopedomIds());
        }
        if (institutionPage.getAutoDistribute() == null) {
            institution.setAutoDistribute(PropertyUtil.FLOW_DISTRIBUTE);
        } else {
            institution.setAutoDistribute(institutionPage.getAutoDistribute());
        }
        institution.setAutoDistribute(PropertyUtil.NUM_1);
        institution.setPopedomSet(new HashSet<>(popedomList));
        institutionService.save(institution);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 查询单个机构
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<InstitutionPage> getOne(@PathVariable Long id) {
        Institution institution = institutionService.find(id);
        InstitutionPage institutionPage = new InstitutionPage();
        entityToPage(institution, institutionPage);
        return ResponseEntity.ok(institutionPage);
    }

    /**
     * 分页查询机构列表
     *
     * @param page
     * @param size
     * @param institutionQuery
     * @param orderQuery
     * @return
     */
    @GetMapping
    public ResponseEntity<PageResult> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                           @RequestParam(value = "size", defaultValue = "20") Integer size,
                                           InstitutionQuery institutionQuery, OrderQuery orderQuery) {
        //  1.获得机构所有子集
        if (institutionQuery.getParentId() != null) {
            List<Long> institutionIds = institutionService.findChildrenId(institutionQuery.getParentId());
            institutionIds.add(institutionQuery.getParentId());
            institutionQuery.setChildrenIds(institutionIds);
        }
        //  2.查询列表
        Page<Institution> pageList = institutionService.getInstitution(OrderQuery.getQuery(orderQuery, page, size), institutionQuery);
        //  3.拼接PageResult
        List<InstitutionPage> institutionPageList = new ArrayList<>();
        List<Institution> institutionList = pageList.getContent();
        if (CollectionUtils.isNotEmpty(institutionList)) {
            for (Institution institution : institutionList) {
                InstitutionPage institutionPage = new InstitutionPage();
                entityToPage(institution, institutionPage);
                institutionPageList.add(institutionPage);
            }
        }
        PageResult pageResult = new PageResult(pageList.getPageable(), institutionPageList, pageList.getTotalElements());
        return ResponseEntity.ok(pageResult);
    }


    /**
     * 删除机构
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResult> delete(@PathVariable Long id) {
        Institution institution = institutionService.find(id);
        institution.setIsDelete(1);
        institutionService.save(institution);
        return new ResponseEntity<ApiResult>(HttpStatus.NO_CONTENT);
    }

    /**
     * 修改机构
     *
     * @param institutionPage
     * @return
     */
    @PutMapping
    public ResponseEntity<ApiResult> update(@RequestBody InstitutionPage institutionPage) {
        Institution institution = new Institution();
        BeanUtils.copyProperties(institutionPage, institution);
        Institution source = institutionService.find(institutionPage.getId());
        //权限修改
        if (ArrayUtils.isNotEmpty(institutionPage.getPopedomIds())) {
            List<Popedom> updatePopedom = popedomService.findByIds(institutionPage.getPopedomIds());
            List<Popedom> initialPopedom = new ArrayList<>(source.getPopedomSet());
            getLessPopedom(initialPopedom, updatePopedom, source);
        }
        UpdateUtil.copyNonNullProperties(institution, source);
        if (institutionPage.getParentId() == null) {
            source.setParentId(0L);
        } else {
            source.setParentId(institutionPage.getParentId());
        }
        if (ArrayUtils.isNotEmpty(institutionPage.getPopedomIds())) {
            source.setPopedomSet(new HashSet<>(popedomService.findByIds(institutionPage.getPopedomIds())));
        }
        if (institutionPage.getAutoDistribute() == null) {
            institution.setAutoDistribute(PropertyUtil.FLOW_DISTRIBUTE);
        } else {
            institution.setAutoDistribute(institutionPage.getAutoDistribute());
        }
        institutionService.save(source);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("all")
    public ResponseEntity<List<InstitutionPage>> getAll() {
        List<InstitutionPage> institutionPageList = new ArrayList<>();
        Institution institutionQuery = new Institution();
        institutionQuery.setIsDelete(PropertyUtil.DELETE_STATUS_FALSE);
        Example<Institution> institutionExample = Example.of(institutionQuery);
        List<Institution> institutionList = institutionService.findAll(institutionExample, Sort.by("id"));
        for (Institution institution : institutionList) {
            InstitutionPage institutionPage = new InstitutionPage();
            entityToPage(institution, institutionPage);
            institutionPageList.add(institutionPage);
        }
        return ResponseEntity.ok(institutionPageList);
    }

    @PutMapping("update-auto")
    public ResponseEntity<ApiResult> updateAuto(@RequestBody InstitutionPage institutionPage) {
        if (institutionPage.getId() != null) {
            Institution institution = institutionService.find(institutionPage.getId());
            institution.setAutoDistribute(institutionPage.getAutoDistribute());
            institutionService.save(institution);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("tree")
    public ResponseEntity<List<InstitutionPage>> getTree() {
        Institution query = new Institution();
        query.setIsDelete(PropertyUtil.DELETE_STATUS_FALSE);
        Example<Institution> institutionExample = Example.of(query);
        List<Institution> institutionList = institutionService.findAll(institutionExample, Sort.by("id"));
        List<InstitutionPage> institutionPageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(institutionList)) {
            List<InstitutionPage> treeList = new ArrayList<>();
            List<InstitutionPage> topTreeList = new ArrayList<>();
            for (Institution institution : institutionList) {
                InstitutionPage institutionPage = new InstitutionPage();
                institutionPage.setLabel(institution.getInstitutionName());
                institutionPage.setValue(institution.getId());
                institutionPage.setChildren(new ArrayList<>());
                institutionPage.setParentId(institution.getParentId());
                treeList.add(institutionPage);
                if (institution.getParentId() == 0L) {
                    topTreeList.add(institutionPage);
                }
            }
            for (InstitutionPage institutionPage : topTreeList) {
                TreeToolUtils.createTree(treeList, institutionPage, "value", "parentId", "children");
                institutionPageList.add(institutionPage);
            }
        }
        return ResponseEntity.ok(institutionPageList);
    }

    @GetMapping("tree-group")
    public ResponseEntity<List<InstitutionPage>> getTreeGroup() {
        Institution query = new Institution();
        query.setIsDelete(PropertyUtil.DELETE_STATUS_FALSE);
        Example<Institution> institutionExample = Example.of(query);
        List<Institution> institutionList = institutionService.findAll(institutionExample, Sort.by("id"));
        // 1.获得所有子级元素
        List<Institution> childrenList = new ArrayList<>();
        for (Institution institution : institutionList) {
            List<Institution> institutions = institutionService.findInstitution(institution.getId());
            if (CollectionUtils.isEmpty(institutions)) {
                childrenList.add(institution);
            }
        }
        //  2.通过子级找到所有父级（倒数第二级）
        Set<Institution> parentInstitutionList = new HashSet<>();
        for (Institution institution : childrenList) {
            //  2.1判断是否是第一级
            if (institution.getParentId().equals(0L)) {
                parentInstitutionList.add(institution);
            } else {
                Institution parentInstitution = institutionService.find(institution.getParentId());
                parentInstitutionList.add(parentInstitution);
            }
        }
        //  3.遍历所有父级
        List<InstitutionPage> institutionPageList = new ArrayList<>();
        for (Institution institution : parentInstitutionList) {
            InstitutionPage institutionPage = new InstitutionPage();
            List<Institution> children = institutionService.findChildren(institution.getId());
            List<InstitutionPage> childrenPage = new ArrayList<>();
            if (CollectionUtils.isEmpty(children)) {
                InstitutionPage page = new InstitutionPage();
                page.setInstitutionName(institution.getInstitutionName());
                page.setId(institution.getId());
                childrenPage.add(page);
            }
            for (Institution i : children) {
                InstitutionPage page = new InstitutionPage();
                page.setInstitutionName(i.getInstitutionName());
                page.setId(i.getId());
                childrenPage.add(page);
            }
            institutionPage.setChildren(childrenPage);
            if (institution.getParentId().equals(0L)) {
                if (CollectionUtils.isEmpty(children)) {
                    institutionPage.setParentsName(PropertyUtil.DEFAULT_NULL);
                } else {
                    institutionPage.setParentsName(institutionService.find(institution.getId()).getInstitutionName());
                }
            } else {
                String parentName = institutionService.getInstitutionName(institutionService.find(institution.getId()));
                institutionPage.setParentsName(parentName);
            }
            institutionPageList.add(institutionPage);
        }
        return ResponseEntity.ok(institutionPageList);
    }


    public void entityToPage(Institution institution, InstitutionPage institutionPage) {
        BeanUtils.copyProperties(institution, institutionPage);
        institutionPage.setId(institution.getId());
        /** 权限 */
        List<Long> popedomIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(institution.getPopedomSet())) {
            for (Popedom popedom : institution.getPopedomSet()) {
                popedomIds.add(popedom.getId());
            }
        }
        institutionPage.setPopedomIds(CommonUtils.list2Array(popedomIds));
        /** id 名字 path */
        if (StringUtils.isNotBlank(institution.getInstitutionPath())) {
            String[] strings = institution.getInstitutionPath().split(",");
            Long[] pathNumber = (Long[]) ConvertUtils.convert(strings, Long.class);
            institutionPage.setInstitutionPath(pathNumber);
            institutionPage.setParentsName(institutionService.getInstitutionName(institution));
        } else {
            institutionPage.setParentsName(PropertyUtil.DEFAULT_NULL);
        }
    }

    public void getLessPopedom(List<Popedom> initialPopedom, List<Popedom> updatePopedom, Institution institution) {
        //  1.遍历修改权限集合
        List<Long> popedomIds = new ArrayList<>();
        for (Popedom popedom : initialPopedom) {
            if (!updatePopedom.contains(popedom)) {
                popedomIds.add(popedom.getId());
            }
        }
        //  没有修改的权限集合  就没必要继续遍历了
        if (CollectionUtils.isEmpty(popedomIds)) {
            return;
        }
        //  2.获得所有机构子集和子集的集合 和 子级下角色的集合
        List<Long> institutionIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Institution> institutionList = institutionService.findChildren(institution.getId());
        institutionList.add(institution);
        for (Institution i : institutionList) {
            institutionIds.add(i.getId());
            List<Position> positionList = positionService.getPosition(i.getId());
            if (CollectionUtils.isNotEmpty(positionList)) {
                for (Position p : positionList) {
                    positionIds.add(p.getId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(institutionIds)) {
            institutionService.deleteInstitutionPopedom(institutionIds, popedomIds);
        }
        if (CollectionUtils.isNotEmpty(positionIds)) {
            positionService.deletePositionPopedom(popedomIds, popedomIds);
        }
    }
}