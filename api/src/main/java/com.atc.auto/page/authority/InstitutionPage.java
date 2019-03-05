package com.atc.auto.page.authority;

import com.atc.auto.page.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * InstitutionPage  机构页面类
 *
 * @author Monco
 * @version 1.0.0
 */
@Data
public class InstitutionPage extends BasePage {

    private static final long serialVersionUID = 379504296323292513L;

    /** 机构名称 */
    private String institutionName;
    /** 上级机构id */
    private Long parentId;
    /** 父级机构名称 */
    private String parentsName;
    /** 机构编码 */
    private String institutionCode;
    /** 权限id集合 */
    private Long[] popedomIds;
    /** 机构 */
    private Long[] institutionPath;
    /** 机构层级 */
    private Integer institutionLevel;
    /** 内部资金模块 0 手动分配  1 自动分配 */
    private Integer autoDistribute;

    /** 树型结构 */
    private String label;
    private Long value;
    private List<InstitutionPage> children;
}