package com.pinyougou.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 杜少
 * @WeChat:haoliyoupad
 * @Title：PageResult类
 * @Date: 2018/9/30 16:54
 * @Description:封装分页信息公共实体类
 * @Version：1.0
 */

public class PageResult implements Serializable {

    //总记录数
    private Long total;

    //数据
    private List<?> row;

    public PageResult() {
    }

    public PageResult(Long total, List<?> row) {
        this.total = total;
        this.row = row;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRow() {
        return row;
    }

    public void setRow(List<?> row) {
        this.row = row;
    }
}
