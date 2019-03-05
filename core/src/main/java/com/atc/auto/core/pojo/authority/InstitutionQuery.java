package com.atc.auto.core.pojo.authority;

import com.atc.auto.core.entity.authority.Institution;
import lombok.Data;

import java.util.List;

/**
 * InstitutionQuery     机构查询对象
 *
 * @author Monco
 * @version 1.0.0
 */
@Data
public class InstitutionQuery extends Institution {

    private static final long serialVersionUID = 2382462838968189138L;
    /** 所有子集id */
    private List<Long> childrenIds;
}