package com.atc.auto.page.authority;

import com.atc.auto.page.base.BasePage;
import lombok.Data;

/**
 * PositionPage
 *
 * @author Monco
 * @version 1.0.0
 */
@Data
public class PositionPage extends BasePage {

    private static final long serialVersionUID = -3517153569042643365L;

    /** 岗位编码 */
    private String positionCode;
    /** 岗位名字 */
    private String positionName;
    /** 机构名称 */
    private String institutionName;
    /** 机构ID */
    private Long institutionId;
    /** 权限id集合 */
    private Long[] popedomIds;
    /** 机构数组 */
    private Long[] institutionPath;
    /** 模板名称 */
    private String templateName;

}