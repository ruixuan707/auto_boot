package com.atc.auto.api.authority;

import com.atc.auto.common.bean.ApiResult;
import com.atc.auto.common.util.CommonUtils;
import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.common.util.UpdateUtil;
import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.entity.authority.Popedom;
import com.atc.auto.core.entity.authority.Position;
import com.atc.auto.core.entity.authority.PositionTemplate;
import com.atc.auto.core.pojo.authority.PositionQuery;
import com.atc.auto.core.pojo.base.OrderQuery;
import com.atc.auto.core.service.authority.InstitutionService;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.core.service.authority.PositionService;
import com.atc.auto.core.service.authority.PositionTemplateService;
import com.atc.auto.page.authority.PositionPage;
import com.atc.auto.page.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * PositionController   岗位控制类
 *
 * @author Monco
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private PopedomService popedomService;

    @Autowired
    private PositionTemplateService positionTemplateService;


    @PostMapping
    public ResponseEntity<ApiResult> save(@RequestBody PositionPage positionPage) {
        Position position = new Position();
        pageToEntity(positionPage, position);
        //绑定权限
        List<Popedom> popedomList = new ArrayList<>(positionPage.getPopedomIds().length);
        if (ArrayUtils.isNotEmpty(positionPage.getPopedomIds())) {
            popedomList = popedomService.findByIds(positionPage.getPopedomIds());
        }
        position.setPopedomSet(new HashSet<>(popedomList));
        positionService.save(position);
        if (StringUtils.isNotBlank(positionPage.getTemplateName())) {
            PositionTemplate positionTemplate = new PositionTemplate();
            positionTemplate.setInstitutionPath(position.getInstitutionPath());
            positionTemplate.setInstitution(position.getInstitution());
            positionTemplate.setTemplateName(positionPage.getTemplateName());
            positionTemplate.setPopedomSet(new HashSet<>(popedomList));
            positionTemplateService.save(positionTemplate);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResult> getList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            PositionQuery positionQuery, OrderQuery orderQuery) {
        Page<Position> pageList = positionService.getPosition(OrderQuery.getQuery(orderQuery, page, size), positionQuery);
        List<Position> positionList = pageList.getContent();
        List<PositionPage> positionPageList = new ArrayList<>();
        entityToPageList(positionList, positionPageList);
        PageResult pageResult = new PageResult(pageList.getPageable(), positionPageList, pageList.getTotalElements());
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("{id}")
    public ResponseEntity<PositionPage> getOne(@PathVariable Long id) {
        PositionPage positionPage = new PositionPage();
        Position position = positionService.find(id);
        entityToPage(position, positionPage);
        return ResponseEntity.ok(positionPage);
    }

    @GetMapping("all")
    public ResponseEntity<List<PositionPage>> getAll(@RequestParam(required = false) Long institutionId) {
        List<PositionPage> positionPageList = new ArrayList<>();
        List<Position> positionList = new ArrayList<>();
        if (institutionId != null) {
            Institution institution = institutionService.find(institutionId);
            List<Long> institutionIds = new ArrayList<>();
            List<Institution> institutionList = new ArrayList<>();

            if (!institution.getParentId().equals(0L)) {
                institutionList.add(institutionService.find(institution.getParentId()));
                institutionList.add(institution);
            } else {
                institutionList.add(institution);
            }
            for (Institution i : institutionList) {
                institutionIds.add(i.getId());
            }
            positionList = positionService.getPosition(institutionIds);
        } else {
            positionList = positionService.getPosition();
        }
        entityToPageList(positionList, positionPageList);
        return ResponseEntity.ok(positionPageList);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<ApiResult> delete(@PathVariable Long id) {
        Position position = positionService.find(id);
        position.setIsDelete(1);
        positionService.save(position);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<ApiResult> update(@RequestBody PositionPage positionPage) {
        Position position = new Position();
        pageToEntity(positionPage, position);
        Position source = positionService.find(positionPage.getId());
        UpdateUtil.copyNonNullProperties(position, source);
        //修改权限
        List<Popedom> popedomList = new ArrayList<>(positionPage.getPopedomIds().length);
        if (ArrayUtils.isNotEmpty(positionPage.getPopedomIds())) {
            popedomList = popedomService.findByIds(positionPage.getPopedomIds());
            source.setPopedomSet(new HashSet<>(popedomList));
        }
        positionService.save(source);
        //修改模板
        if (StringUtils.isNotBlank(positionPage.getTemplateName())) {
            PositionTemplate positionTemplate = new PositionTemplate();
            positionTemplate.setInstitutionPath(source.getInstitutionPath());
            positionTemplate.setInstitution(source.getInstitution());
            positionTemplate.setTemplateName(positionPage.getTemplateName());
            positionTemplate.setPopedomSet(new HashSet<>(popedomList));
            positionTemplateService.save(positionTemplate);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    public void entityToPageList(List<Position> positionList, List<PositionPage> positionPageList) {
        if (CollectionUtils.isNotEmpty(positionList)) {
            for (Position position : positionList) {
                PositionPage positionPage = new PositionPage();
                entityToPage(position, positionPage);
                positionPageList.add(positionPage);
            }
        }
    }

    public void entityToPage(Position position, PositionPage positionPage) {
        BeanUtils.copyProperties(position, positionPage);
        positionPage.setId(position.getId());
        /** 权限 */
        List<Long> popedomIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(position.getPopedomSet())) {
            for (Popedom popedom : position.getPopedomSet()) {
                popedomIds.add(popedom.getId());
            }
        }
        positionPage.setPopedomIds(CommonUtils.list2Array(popedomIds));
        /** 机构id */
        if (position.getInstitution() != null) {
            positionPage.setInstitutionId(position.getInstitution().getId());
        }
        /** id 名字 path */
        if (StringUtils.isNotBlank(position.getInstitutionPath())) {
            String[] strings = position.getInstitutionPath().split(",");
            Long[] pathNumber = (Long[]) ConvertUtils.convert(strings, Long.class);
            positionPage.setInstitutionPath(pathNumber);
            List<Institution> institutionList = institutionService.findByIds(pathNumber);
            StringBuilder sb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(institutionList)) {
                for (int i = 0; i < institutionList.size(); i++) {
                    if (i != 0) {
                        sb.append(" / ");
                    }
                    sb.append(institutionList.get(i).getInstitutionName());
                }
                positionPage.setInstitutionName(sb.toString());
            }
        } else {
            positionPage.setInstitutionName(PropertyUtil.DEFAULT_NULL);
        }
    }

    public void pageToEntity(PositionPage positionPage, Position position) {
        BeanUtils.copyProperties(positionPage, position);
        //修改机构
        if (positionPage.getInstitutionId() != null) {
            position.setInstitution(institutionService.find(positionPage.getInstitutionId()));
        }
        if (ArrayUtils.isNotEmpty(positionPage.getInstitutionPath())) {
            position.setInstitutionPath(CommonUtils.LongArray2String(positionPage.getInstitutionPath()));
        }
    }
}