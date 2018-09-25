package com.xsxx.service;

import com.alibaba.fastjson.JSONObject;
import com.xsxx.entity.Message;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendWXserviceImpl implements SendWXservice {
    @Override
    public void sendWXmsg(String ip, String content, String platform) {
// 获取微信身份标识
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String token = getWeixinToken();
        if (token == null) {
            return ;
        }
        try {
            HttpPost httpPost = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token);
            Message message = new Message();
            //tag表述报警类型
            message.setTotag("2");
//            message.setToparty("");
            message.setAgentid(3);
            message.setMsgtype("text");
            HashMap textMap = new HashMap();
            textMap.put("content", content + " (" + (platform != null ? platform : "") + ip + ")");
            message.setText(textMap);
            message.setSafe(0);
            httpPost.setEntity(new StringEntity(JSONObject.toJSONString(message), "utf-8"));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                if (null != httpEntity) {
                    String retSrc = EntityUtils.toString(httpEntity);
                    System.out.println(retSrc);
                    JSONObject result = JSONObject.parseObject(retSrc);
                    String errorCode = result.getString("errcode");
                    if (errorCode == null) {
                        return ;
                    } else {
                        return ;
                    }
                } else {
                    System.out.println("post httpEntity = null");
                    return ;
                }
            } else {
                System.out.println("post status:\t" + response.getStatusLine().getStatusCode());
                return ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 微信token
    static String wxToken = null;
    // 过期时间
    static Long wxTokenExpireTime = 0L;

    /**
     * 设置 -> 权限管理
     */
    public final static  String cropId = "wx8e1c59bef7d57d5d";
    public final static String secret = "rP2a2VJjhDr0wSVzumH-cbAX9xFsbP7CIHIEBQLnsPBfCu3jzNS3I6nU53LMaucs";

    /**
     * 获取微信token
     * url: https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx8e1c59bef7d57d5d&corpsecret=rP2a2VJjhDr0wSVzumH-cbAX9xFsbP7CIHIEBQLnsPBfCu3jzNS3I6nU53LMaucs
     *
     * @return String token
     */
    public static String getWeixinToken() {
        Long nowSeconds = System.currentTimeMillis()/1000;
        if(wxToken != null && nowSeconds - wxTokenExpireTime < 3600){
            return wxToken;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + cropId + "&corpsecret=" + secret);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                if (null != httpEntity) {
                    String retSrc = EntityUtils.toString(httpEntity);
                    JSONObject jsonObject = JSONObject.parseObject(retSrc);
                    String errorCode = jsonObject.getString("errcode");
                    if (errorCode == null || jsonObject.get("errcode").equals(0)) {
                        wxTokenExpireTime = nowSeconds;
                        wxToken =  jsonObject.getString("access_token");
                        System.out.println(wxTokenExpireTime);
                        System.out.println(wxToken);
                        return wxToken;
                    } else {
                        wxToken = null;
                        wxTokenExpireTime = 0L;
                        System.out.println("getWeixinToken err :" + retSrc);
                    }
                } else {
                    System.out.println("get httpEntity = null");
                }
            } else {
                System.out.println("get status:\t" + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response != null){
                try{
                    response.close();
                }catch (Exception e){

                }
            }
        }
        return null;
    }


    /**
     * 发送消息到微信
     * url: https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=l9yCO7UkyP-BZT9xxk87qItwhTtljHndvNSkhAk1mDLqqRXvMD1PqTRCr0FCwT9d
     * 错误码：
     * 0    发送成功
     * 1    获取token失败
     * 2    缺少参数
     * 3    发送消息失败（网络原因）
     * 4    IP限制
     * 5    忽略
     * 6    没有定义的code
     * {开头的字符串    发送错误（字段）
     *
     *
     * {
     "touser":"@all",
     "toparty":"",
     "totag":"",
     "msgtype":"text",
     "agentid":3,
     "text":{
     "content":"自定义消息"
     },
     "safe":0
     }
     * @return Integer
     */

    /**
     * 运营-1
     * 运维-2
     * 联通运营-3
     * 联通运维-4
     * 芊云运营-5
     * 芊云运维-6
     * 上海尔坤运营-7
     * 上海尔坤运维-8
     * 江西报警组-9
     * 江西报警组-10
     * 43深圳客户报警组-11
     * 43无锡客户报警组-12
     * 43上海客户报警组-13
     */
    // 默认报警组
    Map<Integer, String> defaultCodeMap = new HashMap<Integer, String>() {{
        put(100, "1");//余额预警
        put(101, "2");//入库告警
        put(102, "1");//通道积压告警
        put(103, "2");//通道超时告警
        put(104, "2");//通道重连告警
        put(105, "1|2");//通道反馈异常告警
        put(106, "1|2");//上行异常
        put(107, "2");//系统信号修改通知
        put(108, "11");//深圳客户报警组
        put(109, "12");//上海客户报警组
        put(110, "13");//无锡客户报警组
    }};
    // 阿里芊云报警组
    Map<Integer, String> aliCodeMap = new HashMap<Integer, String>() {{
        put(100, "5");//余额预警
        put(101, "6");//入库告警
        put(102, "5");//通道积压告警
        put(103, "6");//通道超时告警
        put(104, "6");//通道重连告警
        put(105, "5|6");//通道反馈异常告警
        put(106, "5|6");//上行异常
    }};
    // 上海尔坤警组
    Map<Integer, String> erKunCodeMap = new HashMap<Integer, String>() {{
        put(100, "7");//余额预警
        put(101, "8");//入库告警
        put(102, "7");//通道积压告警
        put(103, "8");//通道超时告警
        put(104, "8");//通道重连告警
        put(105, "7|8");//通道反馈异常告警
        put(106, "7|8");//上行异常
    }};
    // 深圳联通报警组
    Map<Integer, String> szUnicomCodeMap = new HashMap<Integer, String>() {{
        put(100, "3");//余额预警
        put(101, "4");//入库告警
        put(102, "3");//通道积压告警
        put(103, "4");//通道超时告警
        put(104, "4");//通道重连告警
        put(105, "3|4");//通道反馈异常告警
        put(106, "3|4");//上行异常
    }};
    // 江西报警组
    Map<Integer, String> jiangxiCodeMap = new HashMap<Integer, String>() {{
        put(100, "9");//余额预警
        put(101, "9");//入库告警
        put(102, "9");//通道积压告警
        put(103, "9");//通道超时告警
        put(104, "9");//通道重连告警
        put(105, "9");//通道反馈异常告警
        put(106, "9");//上行异常
        put(107, "9");//系统信号修改通知
    }};

}
