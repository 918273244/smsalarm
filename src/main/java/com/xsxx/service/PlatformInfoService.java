package com.xsxx.service;

import com.xsxx.pojo.PlatformInfo;

import java.util.List;

public interface PlatformInfoService {


    void addPlatform(PlatformInfo platformInfo);

    List<PlatformInfo> findByPage();

    PlatformInfo findById(Integer id);

    void updatePlatform(PlatformInfo platformInfo);

}
