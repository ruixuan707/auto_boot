package com.atc.auto.core.entity.authority;

import com.atc.auto.core.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Institution  机构（组织架构）
 *
 * @author Monco
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@Table(name = "base_institution")
public class Institution extends BaseEntity<Long> {

    private static final long serialVersionUID = -7904668586317943699L;
    /** 机构名称 */
    private String institutionName;
    /** 上级机构id */
    private Long parentId;
    /** 机构编码 */
    private String institutionCode;
    /** 机构路径 */
    private String institutionPath;
    /** 机构权限 */
    private Set<Popedom> popedomSet = new HashSet<>();
    /** 内部资金模块 0 手动分配  1 自动分配 */
    private Integer autoDistribute;
    /** 层级 */
    private Integer institutionLevel;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "base_institution_popedom", joinColumns = @JoinColumn(name = "institution_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "popedom_id", referencedColumnName = "id"))
    public Set<Popedom> getPopedomSet() {
        return popedomSet;
    }
}