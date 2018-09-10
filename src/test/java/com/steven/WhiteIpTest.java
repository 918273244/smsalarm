package com.steven;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.xsxx.pojo.WhiteIp;
import com.xsxx.service.WhiteIpService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WhiteIpTest extends AppTests {

    @Autowired
    private WhiteIpService whiteIpService;

    @Test
    public void insert(){
        WhiteIp whiteIp = new WhiteIp();
        whiteIp.setIp("192.168.0.1");
        whiteIp.setWname("本地地址");
        whiteIpService.addWhiteIp(whiteIp);
    }

    @Test
    public void update(){
        WhiteIp whiteIp = new WhiteIp();
        whiteIp.setId(2);
        whiteIp.setIp("192.168.0.3");
        whiteIp.setWname("333本地地址");
        whiteIpService.updateWhiteIp(whiteIp);
    }

    @Test
    public void list(){
        Page<WhiteIp> whiteIps = whiteIpService.findByPage(1,2);
        for (WhiteIp w: whiteIps) {
            System.out.println(w.getId());
        }
        System.out.println(whiteIps.getTotal()+"; "+whiteIps.getPages());
    }

    @Test
    public void load(){
        WhiteIp whiteIp = whiteIpService.load(1);
        System.out.println(whiteIp.getWname());
    }



}
