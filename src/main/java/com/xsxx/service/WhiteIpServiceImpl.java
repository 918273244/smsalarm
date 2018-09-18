package com.xsxx.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xsxx.config.springcontextutil.SpringContextHolder;
import com.xsxx.exception.ServiceException;
import com.xsxx.mapper.WhiteIpMapper;
import com.xsxx.pojo.User;
import com.xsxx.pojo.WhiteIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WhiteIpServiceImpl implements WhiteIpService , ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    WhiteIpMapper whiteIpMapper;
    //白名单集合
    List<WhiteIp> list;
    //白名单缓存
    List<String> whiteIpList;

    @Override
    public void addWhiteIp(WhiteIp whiteIp) throws ServiceException {
        try {
            whiteIpMapper.insert(whiteIp);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Page<WhiteIp> findByPage(int pageNo, int pageSize) {
        try {
            PageHelper.startPage(pageNo, pageSize);
            return whiteIpMapper.findByPage();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public List<WhiteIp> findAll() {
        try {
            return whiteIpMapper.findAll();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateWhiteIp(WhiteIp whiteIp) {
        try {
            whiteIpMapper.update(whiteIp);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public WhiteIp load(Integer id) {
        try {
            return whiteIpMapper.load(id);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<WhiteIp> getWhiteIpList() {
        return list;
    }

    @Override
    public List<String> getWhiteIps() {
        return whiteIpList;
    }


    /**
     * 初始化以后得到所有白名单
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        list =  whiteIpMapper.findAll();
        whiteIpList = new ArrayList<>();
        for (WhiteIp whiteIp:list) {
            whiteIpList.add(whiteIp.getIp());
        }
    }
}
