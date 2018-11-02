package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：Areas类
 * @Date: 2018/9/29 16:55
 * @Description:区域实体类
 * @Version：1.0
 */

@Table(name="tb_areas")
public class Areas implements Serializable{

    /** 主键id */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	/** 区域id */
	@Column(name="areaid")
    private String areaId;
	/** 区域名称 */
	@Column(name="area")
    private String area;
	/** 城市id */
	@Column(name="cityid")
    private String cityId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAreaId() {
        return areaId;
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getCityId() {
        return cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}