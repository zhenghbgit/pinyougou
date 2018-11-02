package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：ItemCat类
 * @Date: 2018/9/29 16:59
 * @Description:商品类型（类目）实体类
 * @Version：1.0
 */

@Table(name="tb_item_cat")
public class ItemCat implements Serializable{
	private static final long serialVersionUID = -5192195713480494105L;
	/** 主键id */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	/** 父级id */
	@Column(name="parent_id")
    private Long parentId;
    /** 类型名称 */
	@Column(name="name")
    private String name;
    /** 类型模版id */
	@Column(name="type_id")
    private Long typeId;
    /** setter and getter method */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}