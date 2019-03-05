package com.atc.auto.core.entity.authority;

import com.atc.auto.core.entity.base.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * PositionTemplate
 *
 * @author Monco
 * @version 1.0.0
 */
@Entity
@Table(name = "base_position_template")
public class PositionTemplate extends BaseEntity<Long> {

    private static final long serialVersionUID = 6816096032072369777L;
    /**
     * 模板名字
     */
    private String templateName;
    /**
     * 权限
     */
    private Set<Popedom> popedomSet = new HashSet<>();
    /**
     * 机构
     */
    private Institution institution;
    /**
     * 机构path
     */
    private String institutionPath;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "base_position_template_popedom", joinColumns = @JoinColumn(name = "position_template_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "popedom_id", referencedColumnName = "id"))
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
}