package com.pinyougou.pojo;

import java.io.Serializable;
import java.util.List;


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
