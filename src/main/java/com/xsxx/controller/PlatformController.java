package com.xsxx.controller;

import com.xsxx.entity.JSONResult;
import com.xsxx.pojo.PlatformInfo;
import com.xsxx.pojo.WhiteIp;
import com.xsxx.service.PlatformInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Auther: Steven 孙
 * @Date: 2018/9/18 15:38
 * @Description: 平台基础信息
 */
@Controller
@RequestMapping("platform")
public class PlatformController extends BaseController {

    @Autowired
    PlatformInfoService platformInfoService;

    @RequestMapping("list")
    public String platformView(){
        return "platform/list";
    }

    @RequestMapping("get")
    @ResponseBody
    public JSONResult get(int pageNo, int pageSize){
        try{
            List<PlatformInfo> list =  platformInfoService.findByPage(pageNo, pageSize);
            return ajaxSuccess(list);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
    }

    /**
     * 添加平台信息
     * @param platformInfo
     * @return
     */
    @RequestMapping(value = "addPlatform",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult addPlatform(PlatformInfo platformInfo){
        try{
            platformInfoService.addPlatform(platformInfo);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }

    /**
     * 更新平台信息
     * @param platformInfo
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult update(PlatformInfo platformInfo){
        try {
            platformInfoService.updatePlatform(platformInfo);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }

    /**
     * 获取基本信息
     * @param id
     * @return
     */
    @RequestMapping(value = "platformInfo",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult platformInfo(Integer id){
        PlatformInfo platformInfo;
        try {
            platformInfo = platformInfoService.findById(id);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess(platformInfo);
    }

    /**
     * 删除功能
     * @param id
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public  JSONResult deleteById(Integer id){
        try{
            platformInfoService.deleteById(id);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }


}
