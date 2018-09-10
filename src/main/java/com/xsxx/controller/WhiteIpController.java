package com.xsxx.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xsxx.entity.JSONResult;
import com.xsxx.pojo.WhiteIp;
import com.xsxx.service.WhiteIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 白名单
 */
@Controller
@RequestMapping("whiteIp")
public class WhiteIpController extends BaseController{

    @Autowired
    WhiteIpService whiteIpService;

    @RequestMapping("list")
    public String whiteView(){
        return "whiteIp/list";
    }

    @RequestMapping("get")
    public JSONResult get(int pageNo, int pageSize){
        try{
            Page<WhiteIp> list =  whiteIpService.findByPage(pageNo, pageSize);
            return ajaxSuccess(list);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }


    }



}
