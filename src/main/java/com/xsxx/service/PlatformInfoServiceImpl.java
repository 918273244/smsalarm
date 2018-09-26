package com.xsxx.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xsxx.exception.ServiceException;
import com.xsxx.mapper.PlatformInfoMapper;
import com.xsxx.pojo.PlatformInfo;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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

            if(platformInfo.getPlatformUrl()!=null){
                for (PlatformInfo pf:platformInfoMap.values()){
                    if(pf.getPlatformUrl()!=null && pf.getPlatformUrl().equals(platformInfo.getPlatformUrl())){
                        throw new ServiceException("平台地址（" + platformInfo.getPlatformUrl() + "）已经存在");
                    }
                }
            }
            platformInfoMapper.addPlatform(platformInfo);
            platformInfos.add(platformInfo);
            platformInfoMap.put(platformInfo.getPlatformUrl(), platformInfo);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<PlatformInfo> findByPage(int pageNo, int pageSize) {
        try {
            List<PlatformInfo> list = new ArrayList<>();
            List<PlatformInfo> plist;
            list.addAll(platformInfoMap.values());

            int endIndex = pageNo*pageSize;
            if (list.size()<pageNo*pageSize){
                endIndex = list.size();
            }
            plist = list.subList((pageNo -1) * pageSize, endIndex);
            return plist;
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
            //修改的时候如果这条记录存在就删除，避免修改platformUrl引来的bug
            for(PlatformInfo info : platformInfoMap.values()){
                //platformUrl修改了才删除
                if (info.getId() == platformInfo.getId() && !info.getPlatformUrl().equals(platformInfo.getPlatformUrl())){
                    platformInfoMap.remove(info.getPlatformUrl());
                    break;
                }
            }
            platformInfoMap.put(platformInfo.getPlatformUrl(), platformInfo);
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
        try {
            PlatformInfo platformInfo = platformInfoMapper.findById(id);
            platformInfoMap.remove(platformInfo.getPlatformUrl());
            platformInfoMapper.delete(id);

        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
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


    /**
     * 属性拷贝
     * @param src
     * @param target
     */
    public static void copyPropertie(Object target, Object src) throws ServiceException
    {
        if (src == null || target == null)
        {
            return;
        }
        if (target.getClass() != src.getClass())
        {
            throw new ServiceException("不支持不同对象之间的拷贝");
        }
        try
        {
            BeanInfo srcBean = Introspector.getBeanInfo(src.getClass(),
                    src.getClass().getSuperclass());
            BeanInfo targetBean = Introspector.getBeanInfo(target.getClass(),
                    src.getClass().getSuperclass());
            PropertyDescriptor[] srcPropers = srcBean.getPropertyDescriptors();
            PropertyDescriptor[] targetPropers = targetBean.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : srcPropers)
            {
                //有get和set方法的时候，直接使用get和set方法
                String name = propertyDescriptor.getName();
                Method getMethod = propertyDescriptor.getReadMethod();
                if (null != getMethod && null != propertyDescriptor.getWriteMethod())
                {
                    Object val = getMethod.invoke(src);
                    for (PropertyDescriptor property : targetPropers)
                    {
                        if (property.getName().equals(name))
                        {
                            property.getWriteMethod().invoke(target, val);
                            break;
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw new ServiceException(e.getMessage());
        }
    }
}
