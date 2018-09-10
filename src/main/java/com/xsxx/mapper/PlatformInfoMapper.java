package com.xsxx.mapper;

import com.xsxx.pojo.PlatformInfo;

import java.util.List;

public interface PlatformInfoMapper {

    void addPlatform(PlatformInfo platformInfo);

    List<PlatformInfo> findByPage();

    PlatformInfo findById(Integer id);

    void updatePlatform(PlatformInfo platformInfo);

}
