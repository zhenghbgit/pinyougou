package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：Specification类
 * @Date: 2018/9/29 17:01
 * @Description:规格实体类
 * @Version：1.0
 */

@Table(name="tb_specification")
public class Specification implements Serializable{
   
	private static final long serialVersionUID = -972374525762485421L;
	/** 主键  规格编号 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	/** 规格名称 */
	@Column(name="spec_name")
    private String specName;
    /** 规格属性集合 */
	@Transient
    private List<SpecificationOption> specificationOptions; 
    
    /** setter and getter method */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSpecName() {
        return specName;
    }
    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }
	public List<SpecificationOption> getSpecificationOptions() {
		return specificationOptions;
	}
	public void setSpecificationOptions(List<SpecificationOption> specificationOptions) {
		this.specificationOptions = specificationOptions;
	}
}