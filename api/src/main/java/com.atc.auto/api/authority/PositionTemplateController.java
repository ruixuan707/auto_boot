package com.atc.auto.api.authority;

import com.atc.auto.common.util.CommonUtils;
import com.atc.auto.common.util.PropertyUtil;
import com.atc.auto.core.entity.authority.Institution;
import com.atc.auto.core.entity.authority.Popedom;
import com.atc.auto.core.entity.authority.PositionTemplate;
import com.atc.auto.core.pojo.authority.PositionTemplateQuery;
import com.atc.auto.core.pojo.base.OrderQuery;
import com.atc.auto.core.service.authority.InstitutionService;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.core.service.authority.PositionTemplateService;
import com.atc.auto.page.authority.PositionTemplatePage;
import com.atc.auto.page.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * PositionTemplateController
 *
 * @author Monco
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/position-template")
public class PositionTemplateController {

    @Autowired
    private PositionTemplateService positionTemplateService;

    @Autowired
    private InstitutionService institutionService;


    @GetMapping
    public ResponseEntity<PageResult> getList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            PositionTemplateQuery positionTemplateQuery, OrderQuery orderQuery) {
        Page<PositionTemplate> pageList = positionTemplateService.getPositionTemplate(OrderQuery.getQuery(orderQuery, page, size), positionTemplateQuery);
        List<PositionTemplate> positionTemplateList = pageList.getContent();
        List<PositionTemplatePage> positionTemplatePageList = new ArrayList<>();
        entityToPageList(positionTemplateList, positionTemplatePageList);
        PageResult pageResult = new PageResult(pageList.getPageable(), positionTemplatePageList, pageList.getTotalElements());
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("all")
    public ResponseEntity<List<PositionTemplatePage>> getAll(@RequestParam(required = false) Long institutionId) {
        List<PositionTemplatePage> positionTemplatePageList = new ArrayList<>();
        List<PositionTemplate> positionTemplateList = new ArrayList<>();
        if (institutionId != null) {
            positionTemplateList = positionTemplateService.getPositionTemplate(institutionId);
        } else {
            positionTemplateList = positionTemplateService.getPositionTemplate();
        }
        entityToPageList(positionTemplateList, positionTemplatePageList);
        return ResponseEntity.ok(positionTemplatePageList);
    }

    @GetMapping("{id}")
    public ResponseEntity<PositionTemplatePage> getOne(@PathVariable Long id) {
        PositionTemplate positionTemplate = positionTemplateService.find(id);
        PositionTemplatePage positionTemplatePage = new PositionTemplatePage();
        entityToPage(positionTemplate, positionTemplatePage);
        return ResponseEntity.ok(positionTemplatePage);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        PositionTemplate positionTemplate = positionTemplateService.find(id);
        positionTemplate.setIsDelete(1);
        positionTemplateService.save(positionTemplate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public void entityToPageList(List<PositionTemplate> positionTemplateList, List<PositionTemplatePage> positionTemplatePageList) {
        if (CollectionUtils.isNotEmpty(positionTemplateList)) {
            for (PositionTemplate positionTemplate : positionTemplateList) {
                PositionTemplatePage positionTemplatePage = new PositionTemplatePage();
                entityToPage(positionTemplate, positionTemplatePage);
                positionTemplatePageList.add(positionTemplatePage);
            }
        }
    }

    public void entityToPage(PositionTemplate positionTemplate, PositionTemplatePage positionTemplatePage) {
        BeanUtils.copyProperties(positionTemplate, positionTemplatePage);
        positionTemplatePage.setId(positionTemplate.getId());
        /** 权限 */
        List<Long> popedomIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(positionTemplate.getPopedomSet())) {
            for (Popedom popedom : positionTemplate.getPopedomSet()) {
                popedomIds.add(popedom.getId());
            }
        }
        positionTemplatePage.setPopedomIds(CommonUtils.list2Array(popedomIds));
        /** 机构id */
        if (positionTemplate.getInstitution() != null) {
            positionTemplatePage.setInstitutionId(positionTemplate.getInstitution().getId());
        }
        /** id 名字 path */
        if (StringUtils.isNotBlank(positionTemplate.getInstitutionPath())) {
            String[] strings = positionTemplate.getInstitutionPath().split(",");
            Long[] pathNumber = (Long[]) ConvertUtils.convert(strings, Long.class);
            positionTemplatePage.setInstitutionPath(pathNumber);
            List<Institution> institutionList = institutionService.findByIds(pathNumber);
            StringBuilder sb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(institutionList)) {
                for (int i = 0; i < institutionList.size(); i++) {
                    if (i != 0) {
                        sb.append(" / ");
                    }
                    sb.append(institutionList.get(i).getInstitutionName());
                }
                positionTemplatePage.setInstitutionName(sb.toString());
            }
        } else {
            positionTemplatePage.setInstitutionName(PropertyUtil.DEFAULT_NULL);
        }
    }
}