package com.xsxx.constants;

/**
 * 监控状态结果
 */
public enum PlatformStatus {
    ERROR("失败",0),
    SUCCESS("成功", 1);


    private String msg;
    private Integer status;

    PlatformStatus(String msg, Integer status){
        this.msg = msg;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
