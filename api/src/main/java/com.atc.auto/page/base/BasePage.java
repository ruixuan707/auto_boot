package com.atc.auto.page.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * BasePage
 *
 * @author Mengke
 * @version 1.0.0
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BasePage implements Serializable {

    private static final long serialVersionUID = 6470839785182956096L;
    private Long id;
    private Integer status;
    private Integer isDelete;
    private String remarks;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;
    private Long[] batchIds;
    private Long createdId;
    private String createdName;
    private Long updatedId;
    private String updatedName;
    private Long version;
}