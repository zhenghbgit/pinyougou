package com.pinyougou.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @QQ:522237454
 * @Title：Brand类
 * @Date: 2018/9/28 15:43
 * @Description:品牌实体类
 * @Version：1.0
 */

@Table(name = "tb_brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = -8315881228799842049L;

    @Id //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长
    @Column(name = "id") //列名
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "first_char")
    private String firstChar;

    public Brand() {
    }

    public Brand(Long id, String name, String firstChar) {
        this.id = id;
        this.name = name;
        this.firstChar = firstChar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

}
