package com.xsxx.mapper;

import com.github.pagehelper.Page;
import com.xsxx.pojo.PlatformInfo;

import java.util.List;

public interface PlatformInfoMapper extends BaseMapper<PlatformInfo, Integer>{

    int addPlatform(PlatformInfo platformInfo);

    Page<PlatformInfo> findByPage();

    PlatformInfo findById(Integer id);

    void updatePlatform(PlatformInfo platformInfo);

}
