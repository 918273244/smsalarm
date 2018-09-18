package com.xsxx.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xsxx.entity.JSONResult;
import com.xsxx.pojo.WhiteIp;
import com.xsxx.service.WhiteIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 获取所有数据
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("get")
    @ResponseBody
    public JSONResult get(int pageNo, int pageSize){
        try{
            Page<WhiteIp> list =  whiteIpService.findByPage(pageNo, pageSize);
            return ajaxSuccess(list);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
    }

    /**
     * 添加白名单
     * @param whiteIp
     * @return
     */
    @RequestMapping(value = "addWhiteIp",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult addWhiteIp(WhiteIp whiteIp){
        try {
            whiteIpService.addWhiteIp(whiteIp);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }

    /**
     * 更新白名单
     * @param whiteIp
     * @return
     */
    @RequestMapping(value = "updateWhiteIp",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult updateWhiteIp(WhiteIp whiteIp){
        try {
            whiteIpService.updateWhiteIp(whiteIp);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }

    /**
     * 获取白名单基本信息
     * @param id
     * @return
     */
    @RequestMapping(value = "whiteIpInfo",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult whiteIpInfo(Integer id){
        WhiteIp whiteIp;
        try {
            whiteIp = whiteIpService.load(id);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess(whiteIp);
    }


    /**
     * 删除功能
     * @param id
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public  JSONResult deleteById(Integer id){
        try{
            whiteIpService.deleteById(id);
        }catch (Exception e){
            return ajaxFail(e.getMessage());
        }
        return ajaxSuccess();
    }



}
