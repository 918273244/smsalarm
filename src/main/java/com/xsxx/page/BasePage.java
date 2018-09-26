package com.xsxx.page;

import java.util.Date;

/**
 * Created by momo on 2016/8/16.
 * 查找model
 */
public class BasePage {
    private int total;      //记录总数
    private int page = 1;       //当前页码,最小值1，否则为非法
    private int count = 1;      //每页数量
    private Date startDate; //开始时间
    private Date endDate;   //结束时间
    private String order;   //排序字段
    private String asc;//排序方式
    //是否加载全部数据，为true时不进行分页，不判断 countAllRecord
    private Boolean loadAllRecord = false;
    //是否获取记录总数等分页参数  false时将不进行count(*)，total和totalPage为null
    private Boolean countTotal = true;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        //过滤掉非法字符
		/*if (order == null || StringUtils.isAccount(order)) {
			this.order = order;
		}*/
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        if (asc == null || "asc".equals(asc)) {
            this.asc = asc;
        } else if ("desc".equals(asc)) {
            this.asc = asc;
        }
    }

    public Boolean getLoadAllRecord() {
        return loadAllRecord;
    }

    public void setLoadAllRecord(Boolean loadAllRecord) {
        this.loadAllRecord = loadAllRecord;
    }

    public Boolean getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Boolean countTotal) {
        this.countTotal = countTotal;
    }

}
