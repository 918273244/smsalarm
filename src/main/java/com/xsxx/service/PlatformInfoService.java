package com.xsxx.service;

import com.github.pagehelper.Page;
import com.xsxx.pojo.PlatformInfo;

import java.util.List;

public interface PlatformInfoService {


    void addPlatform(PlatformInfo platformInfo);

    Page<PlatformInfo> findByPage(int pageNo, int pageSize);

    List<PlatformInfo> findAll();

    PlatformInfo findById(Integer id);

    void updatePlatform(PlatformInfo platformInfo);

}
