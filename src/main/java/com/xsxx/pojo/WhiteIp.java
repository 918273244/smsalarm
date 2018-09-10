package com.xsxx.pojo;

import java.io.Serializable;
import java.util.Date;

public class WhiteIp implements Serializable {

    private Integer id;
    private String ip; //ip地址
    private Date created; //创建时间
    private String wname; // 平台名称

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
