package com.xsxx.service;

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
        platformInfoMapper.addPlatform(platformInfo);
    }

    @Override
    public List<PlatformInfo> findByPage() {
        return platformInfoMapper.findByPage();
    }

    @Override
    public PlatformInfo findById(Integer id) {
        return platformInfoMapper.findById(id);
    }

    @Override
    public void updatePlatform(PlatformInfo platformInfo) {
        platformInfoMapper.updatePlatform(platformInfo);
    }
}
