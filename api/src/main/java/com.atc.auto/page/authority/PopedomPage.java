package com.atc.auto.page.authority;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PopedomPage 返回tree对象
 *
 * @author Mengle
 * @version 1.0.0
 */
@Data
public class PopedomPage implements Serializable {
    /** ID */
    private Long id;
    /** 父级权限ID */
    private Long parentId;
    /** 菜单权限字段名称 */
    private String name;
    /** 菜单权限字段代码 */
    private String code;
    /** 菜单权限字段英文名称 */
    private String keyCode;
    /** 菜单权限字段代码层级路径 */
    private String codePath;
    /** 权限类型 （1菜单2按钮3字段） */
    private Integer type;
    /** 子级权限 */
    private List<PopedomPage> children = new ArrayList<>();
    /** 0为未匹配 1为匹配 */
    private Integer matching;
}