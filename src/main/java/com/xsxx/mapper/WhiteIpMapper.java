package com.xsxx.mapper;

import com.github.pagehelper.Page;
import com.xsxx.pojo.WhiteIp;

import java.util.List;

public interface WhiteIpMapper extends BaseMapper<WhiteIp, Integer>{

    void addWhiteIp(WhiteIp whiteIp);

    /**
     * 分页查询
     * @return
     */
    Page<WhiteIp> findByPage();

    List<WhiteIp> findAll();

    void updateWhiteIp(WhiteIp whiteIp);
}
