package com.xsxx.controller;

import com.xsxx.entity.JSONResult;
import com.xsxx.page.BasePage;
import com.xsxx.pojo.PlatformInfo;
import com.xsxx.pojo.User;
import com.github.pagehelper.Page;
import com.xsxx.service.PlatformInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
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
    public String platformView(Model model){
        UUID uuid = UUID.randomUUID();
        model.addAttribute("uuid", uuid);
        // 获取当前登录用户的角色
        User loginUser = getLoginUser();
        model.addAttribute("user", loginUser);
        return "platform/list";
    }

    /**创建页面*/
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(){
        return "platform/edit";
    }

    /**编辑页面*/
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(Model model, @ RequestParam("id") Integer id){
        PlatformInfo platformInfo = platformInfoService.findById(id);
        model.addAttribute("platformInfo",platformInfo);
        return "platform/edit";
    }


    @RequestMapping("get")
    @ResponseBody
    public JSONResult get(BasePage page){
        try{
            List<PlatformInfo> list =  platformInfoService.findByPage(page.getPage(), page.getCount());
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
    @ResponseBody
    public JSONResult deleteById(Integer id){
        try{
            platformInfoService.deleteById(id);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }


}
