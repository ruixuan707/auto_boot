package com.atc.auto.api.authority;

import com.atc.auto.common.util.TreeToolUtils;
import com.atc.auto.core.entity.authority.Popedom;
import com.atc.auto.core.service.authority.PopedomService;
import com.atc.auto.page.authority.PopedomPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * PopedomController
 *
 * @author Lijin
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("popedom")
public class PopedomController {

    @Autowired
    private PopedomService popedomService;


    @GetMapping("/lazy/tree")
    public ResponseEntity<List<PopedomPage>> getPopedomLazyTree(@RequestParam(value = "parentId", defaultValue = "0") Long parentId,
                                                                @RequestParam(required = false) Long type,
                                                                @RequestParam(required = false) Long positionId,
                                                                @RequestParam(required = false) Long institutionId,
                                                                @RequestParam(required = false) Long templateId) {
        List<Popedom> popedomList = popedomService.getPopedomLazyTree(parentId, positionId, type, institutionId, templateId);
        List<PopedomPage> popedomPages = transformCommon(popedomList, parentId);
        return ResponseEntity.ok(popedomPages);
    }

    @GetMapping("/lazy/tree/mixed")
    public ResponseEntity<List<Long>> getPopedomIdsLazyTree(@RequestParam(value = "parentId", defaultValue = "0") Long parentId,
                                                            @RequestParam(required = false) Long type,
                                                            @RequestParam(required = false) Long positionId,
                                                            @RequestParam(required = false) Long institutionId,
                                                            @RequestParam(required = false) Long templateId) {
        List<Popedom> popedomList = popedomService.getPopedomLazyTree(parentId, positionId, type, institutionId, templateId);
        List<Long> popedomIds = new ArrayList<>();
        for (Popedom popedom : popedomList) {
            popedomIds.add(popedom.getId());
        }
        return ResponseEntity.ok(popedomIds);
    }

    private List<PopedomPage> transformCommon(List<Popedom> popedomList, Long parentId) {
        List<PopedomPage> popedomPageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(popedomList)) {
            List<PopedomPage> treeList = new ArrayList<>();
            List<PopedomPage> topTreeList = new ArrayList<>();
            for (Popedom popedom : popedomList) {
                PopedomPage popedomPage = new PopedomPage();
                popedomPage.setId(popedom.getId());
                popedomPage.setParentId(popedom.getParentId());
                popedomPage.setCode(popedom.getCode());
                popedomPage.setCodePath(popedom.getCodePath());
                popedomPage.setName(popedom.getName());
                popedomPage.setType(popedom.getType());
                popedomPage.setMatching(popedom.getMatching());
                treeList.add(popedomPage);
                if (popedom.getParentId() == parentId) {
                    topTreeList.add(popedomPage);
                }
            }
            for (PopedomPage popedomPage : topTreeList) {
                TreeToolUtils.createTree(treeList, popedomPage, "id", "parentId", "children");
                popedomPageList.add(popedomPage);
            }
        }
        return popedomPageList;
    }
}