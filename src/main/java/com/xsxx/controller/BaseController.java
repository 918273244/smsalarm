package com.xsxx.controller;

import com.xsxx.constants.ErrorCode;
import com.xsxx.entity.JSONResult;
import com.xsxx.pojo.User;
import org.apache.shiro.SecurityUtils;

/**
 * by Steven 2018/9/5
 * 基本controller
 */
public class BaseController {

    /**
     * 从Shrio里获取登陆用户
     * @return
     */
    public User getLoginUser()
    {
        Object pricipal = SecurityUtils.getSubject().getPrincipal();
        if(null != pricipal){
            return (User)pricipal;
        }
        return null;
    }

    /**
     * c成功
     */
    final public JSONResult ajaxSuccess()
    {
        JSONResult ecologyResp = new JSONResult();
        ecologyResp.setMsg("操作成功");
        return ecologyResp;
    }

    final public JSONResult ajaxSuccess(Object data)
    {
        JSONResult ecologyResp = new JSONResult();
        ecologyResp.setResult(data);
        return ecologyResp;
    }

    /**
     * 失败
     */
    final public JSONResult ajaxFail(ErrorCode errorCode)
    {
        JSONResult ecologyResp = new JSONResult();
        ecologyResp.setCode(errorCode.getCode());
        return ecologyResp;
    }

    final public JSONResult ajaxFail(String message)
    {
        JSONResult ecologyResp = new JSONResult();
        ecologyResp.setCode(ErrorCode.CODE.getCode());
        ecologyResp.setMsg(message);
        return ecologyResp;
    }
}
