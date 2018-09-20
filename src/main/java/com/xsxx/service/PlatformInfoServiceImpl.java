package com.xsxx.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xsxx.exception.ServiceException;
import com.xsxx.mapper.PlatformInfoMapper;
import com.xsxx.pojo.PlatformInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlatformInfoServiceImpl implements PlatformInfoService  , ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(("alarmfile"));

    @Autowired
    PlatformInfoMapper platformInfoMapper;

    private List<PlatformInfo> platformInfos;
    private Map<String, PlatformInfo> platformInfoMap;

    @Override
    public void addPlatform(PlatformInfo platformInfo) {
        try {
            platformInfoMapper.addPlatform(platformInfo);
            platformInfos.add(platformInfo);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<PlatformInfo> findByPage(int pageNo, int pageSize) {
        try {
            List<PlatformInfo> list = new ArrayList<>();
            for (PlatformInfo value : platformInfoMap.values()) {
                list.add(value);
            }

//            list.subList(pageNo, pageNo+pageSize);
            return list;
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<PlatformInfo> findAll() {
        try {
            return platformInfos;
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public PlatformInfo findById(Integer id) {
        try {
            return platformInfoMapper.findById(id);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updatePlatform(PlatformInfo platformInfo) {
        try {
            platformInfoMapper.updatePlatform(platformInfo);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Map<String, PlatformInfo> getPlatformMap() {
        return platformInfoMap;
    }

    @Override
    public void deleteById(Integer id) {
        List<PlatformInfo> platformInfoList = new ArrayList<>();
        try {
            for (PlatformInfo p: platformInfos) {
                if(p.getId().intValue() == id.intValue()){
                    continue;
                }
                platformInfoList.add(p);
            }
            platformInfoMapper.delete(id);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        platformInfos  = platformInfoList;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            platformInfos = platformInfoMapper.findByPage();
            platformInfoMap = new ConcurrentHashMap<>();
            for (PlatformInfo p:platformInfos) {
                platformInfoMap.put(p.getPlatformUrl(), p);
            }
        }catch (Exception e){
            logger.error("获取平台列表错误: "+e.getMessage());
        }
    }
}
