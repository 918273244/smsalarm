package com.xsxx.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xsxx.exception.ServiceException;
import com.xsxx.pojo.WhiteIp;

import java.util.List;

public interface WhiteIpService {

    String name = "whiteIpService";

    void addWhiteIp(WhiteIp whiteIp) throws ServiceException;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<WhiteIp> findByPage(int pageNo, int pageSize) throws ServiceException;

    List<WhiteIp> findAll() throws ServiceException;

    void updateWhiteIp(WhiteIp whiteIp) throws ServiceException;

    WhiteIp load(Integer id);

    List<WhiteIp> getWhiteIpList();

}
