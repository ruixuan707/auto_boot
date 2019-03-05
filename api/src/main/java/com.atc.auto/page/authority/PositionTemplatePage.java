package com.atc.auto.page.authority;

import com.atc.auto.page.base.BasePage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * PositionTemplatePage
 *
 * @author Monco
 * @version 1.0.0
 */
@Data
public class PositionTemplatePage extends BasePage {

    private static final long serialVersionUID = -1882417047699314832L;

    /** 权限id集合 */
    private Long[] popedomIds;
    /** 模板名称 */
    private String templateName;
    /** 机构名称 */
    private String institutionName;
    /** 机构ID */
    private Long institutionId;
    /** 机构数组 */
    private Long[] institutionPath;
}