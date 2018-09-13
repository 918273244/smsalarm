package com.xsxx.service;

import com.github.pagehelper.Page;
import com.xsxx.pojo.PlatformInfo;

import java.util.List;
import java.util.Map;

public interface PlatformInfoService {


    void addPlatform(PlatformInfo platformInfo);

    List<PlatformInfo> findByPage(int pageNo, int pageSize);

    List<PlatformInfo> findAll();

    PlatformInfo findById(Integer id);

    void updatePlatform(PlatformInfo platformInfo);

    Map<String, PlatformInfo> getPlatformMap();

}
