package com.liansen.flow.page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CDZ on 2018/11/1.
 */
public class PageList {
    private int page;   //当前页
    private int total;   //总行数
    private int pages;    //总页数
    private List data=new ArrayList();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List getData() {
        if(data==null){
            data=new ArrayList();
        }
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
