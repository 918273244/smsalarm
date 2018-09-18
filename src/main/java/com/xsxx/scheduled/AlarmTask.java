package com.xsxx.scheduled;

import com.xsxx.constants.PlatformStatus;
import com.xsxx.http.asyHttpClient.AsynHttpClientFactory;
import com.xsxx.pojo.PlatformInfo;
import com.xsxx.service.PlatformInfoService;
import com.xsxx.service.SendWXservice;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
@EnableScheduling
@Async
public class AlarmTask {

    private static Logger logger = LoggerFactory.getLogger(("alarmfile"));


    @Autowired
    PlatformInfoService platformInfoService;

    @Autowired
    SendWXservice sendWXservice;

    //报警队列
    ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

    /**第一次延迟5秒后执行，之后按fixedDelay的规则每5秒执行一次*/
    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void task() throws InterruptedException, ExecutionException, IOException {
        List<PlatformInfo> platformInfoList = platformInfoService.findAll();

        Map<String, PlatformInfo> map = platformInfoService.getPlatformMap();
        if(platformInfoList != null && platformInfoList.size()>0){
            int maxSiz = 10;
            if(platformInfoList.size()>maxSiz){
                int from = 0;
                while (from < platformInfoList.size()){
                    List<PlatformInfo> list = platformInfoList.subList(from, Math.min(from + maxSiz, platformInfoList.size()));
                    HttpGet[] urls = new HttpGet[list.size()];
                    for (int i = 0; i < list.size(); i++){
                        urls[i] = new HttpGet(list.get(i).getPlatformUrl());
                    }
                    updateStatus(urls, map);
                    from += maxSiz;
                }
            }else{
                HttpGet[] urls = new HttpGet[platformInfoList.size()];
                for (int i = 0; i < platformInfoList.size(); i++){
                    urls[i] = new HttpGet(platformInfoList.get(i).getPlatformUrl());
                }
                updateStatus(urls, map);
            }
        }
    }

    public void updateStatus(HttpGet[] urls, Map<String, PlatformInfo> map) throws ExecutionException, InterruptedException, IOException {
        CloseableHttpAsyncClient httpClient = AsynHttpClientFactory.getCloseableHttpAsyncClient();
        final CountDownLatch latch = new CountDownLatch(urls.length);
        for(final HttpGet request: urls){

            Future<HttpResponse> future =  httpClient.execute(request,  new FutureCallback(){
                @Override
                public void completed(Object obj) {
                    final HttpResponse response = (HttpResponse)obj;
                    latch.countDown();
                    PlatformInfo platformInfo =  map.get(request.getURI().toString());
                    if( response.getStatusLine().getStatusCode() == 200){
                        if(platformInfo != null){
                            platformInfo.setStatus(PlatformStatus.SUCCESS.getStatus());
                        }
                    }else{
                        if(platformInfo != null){
                            platformInfo.setStatus(PlatformStatus.ERROR.getStatus());
                        }
                    }

                }

                @Override
                public void failed(Exception excptn) {
                    try{
                        latch.countDown();
                        PlatformInfo platformInfo =  map.get(request.getURI().toString());
                        if(platformInfo != null){
                            //添加平台号
                            queue.add(platformInfo.getPname());
                            platformInfo.setStatus(PlatformStatus.ERROR.getStatus());
                        }
                    }catch (Exception e){
                        logger.error("请求连接失败："+e.getMessage());
                    }

                }

                @Override
                public void cancelled() {
                    try{
                        latch.countDown();
                        PlatformInfo platformInfo =  map.get(request.getURI().toString());
                        if(platformInfo != null){
                            queue.add(platformInfo.getPname());
                            platformInfo.setStatus(PlatformStatus.ERROR.getStatus());

                        }
                    }catch (Exception e){
                        logger.error("请求没连接成功："+e.getMessage());
                    }

                }

            });
        }
        try {
            latch.await();
        } catch (Exception ex) {
            logger.error("定时任务出错："+ex.getMessage());
        }
       /* finally{
            try {
                httpClient.close();
            }
            catch (IOException ex) {
                logger.error("定时任务出错："+ex.getMessage());
            }
        }*/

    }


    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    public void take(){
        if(queue.size()>0){
            String pname = queue.poll();
            sendWXservice.sendWXmsg(pname,pname,pname);
            logger.error(pname+" 平台断开");
            try {
                //睡2秒
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



}
