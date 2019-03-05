package com.atc.auto.core.service.authority;

import com.atc.auto.core.entity.authority.PositionTemplate;
import com.atc.auto.core.pojo.authority.PositionTemplateQuery;
import com.atc.auto.core.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * PositionTemplateService
 *
 * @author Monco
 * @version 1.0.0
 */
public interface PositionTemplateService extends BaseService<PositionTemplate, Long> {

    /**
     * 模板分页
     *
     * @param pageable
     * @param positionTemplateQuery
     * @return
     */
    Page<PositionTemplate> getPositionTemplate(Pageable pageable, PositionTemplateQuery positionTemplateQuery);

    /**
     * 按照部门找到模板
     *
     * @param institutionId
     * @return
     */
    List<PositionTemplate> getPositionTemplate(Long institutionId);

    /**
     * 查找角色模板
     *
     * @return
     */
    List<PositionTemplate> getPositionTemplate();
}

