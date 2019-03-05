package com.atc.auto.core.entity.authority;

import com.atc.auto.core.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Position 岗位实体类
 *
 * @author Monco
 * @version 1.0.0
 */
@Entity
@Table(name = "base_position")
public class Position extends BaseEntity<Long> {

    private static final long serialVersionUID = -1620895567904095730L;

    /** 岗位编码 */
    private String positionCode;
    /** 岗位名字 */
    private String positionName;
    /** 权限 */
    private Set<Popedom> popedomSet = new HashSet<>();
    /** 机构 */
    @NotNull
    private Institution institution;
    /** 机构path */
    private String institutionPath;
    /** 是否是模板 */
    private Integer isTemplate;
    /** 模板名字 */
    private String templateName;

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "base_position_popedom", joinColumns = @JoinColumn(name = "position_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "popedom_id", referencedColumnName = "id"))
    public Set<Popedom> getPopedomSet() {
        return popedomSet;
    }

    public void setPopedomSet(Set<Popedom> popedomSet) {
        this.popedomSet = popedomSet;
    }

    @ManyToOne
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public String getInstitutionPath() {
        return institutionPath;
    }

    public void setInstitutionPath(String institutionPath) {
        this.institutionPath = institutionPath;
    }

    public Integer getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Integer isTemplate) {
        this.isTemplate = isTemplate;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}