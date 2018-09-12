package com.xsxx.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xsxx.exception.ServiceException;
import com.xsxx.mapper.PlatformInfoMapper;
import com.xsxx.pojo.PlatformInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformInfoServiceImpl implements PlatformInfoService {

    @Autowired
    PlatformInfoMapper platformInfoMapper;

    @Override
    public void addPlatform(PlatformInfo platformInfo) {
        try {
        platformInfoMapper.addPlatform(platformInfo);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Page<PlatformInfo> findByPage(int pageNo, int pageSize) {
        try {
        PageHelper.startPage(pageNo, pageSize);
        return platformInfoMapper.findByPage();
    }catch (Exception e){
        throw new ServiceException(e.getMessage());
    }
    }

    @Override
    public List<PlatformInfo> findAll() {
        try {
        return platformInfoMapper.findByPage();
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
}
