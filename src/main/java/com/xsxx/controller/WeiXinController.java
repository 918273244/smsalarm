package com.xsxx.controller;

import com.alibaba.fastjson.JSONObject;
import com.xsxx.entity.Message;
import com.xsxx.service.SendWXserviceImpl;
import com.xsxx.service.WhiteIpService;
import com.xsxx.weixin.aes.AesException;
import com.xsxx.weixin.aes.WXBizMsgCrypt;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "weixin")
public class WeiXinController {

    @Autowired
    WhiteIpService whiteIpService;

    /**
     * 设置 -> 权限管理
     */
    public final String cropId = "wx8e1c59bef7d57d5d";
    public final String secret = "rP2a2VJjhDr0wSVzumH-cbAX9xFsbP7CIHIEBQLnsPBfCu3jzNS3I6nU53LMaucs";


    @RequestMapping(value = "test")
    @ResponseBody
    public String returnSuccess() {
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
                        return jsonObject.getString("access_token");
                    } else {
                        return retSrc;
                    }
                } else {
                    return "get httpEntity = null";
                }
            } else {
                return "get status:\t" + response.getStatusLine().getStatusCode();
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
        return "==";
    }

    //调试模式，允许任何IP的请求
    private boolean debug = true;

    @RequestMapping(value = "debug")
    @ResponseBody
    public String debugStateChange() {
        debug = !debug;
        return String.valueOf(debug);
    }

    //访问地址：http://localhost:8080/Test/returnString
    @RequestMapping(value = "testString", produces = {"text/plain;charset=UTF-8"})
    //produces用于解决返回中文乱码问题，application/json;为json解决中文乱码
    @ResponseBody
    public String returnString() {
        return "hello return string 这是中文，并没有乱码";
    }

    //================================================== 微信 ==========================================================
    private WXBizMsgCrypt getWXBizMsgCrypt() throws AesException {
        String sToken = "wQC0aXvsS";
        String sCorpID = "wx8e1c59bef7d57d5d"; //CorpID
        String sEncodingAESKey = "myJ2slYtToV3MeVDBgUOsEcMtJQ9yRmWxr3qJiMefQt";
        return new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
    }


    /**
     * 微信验证,用于在域名服务器和微信服务器之间建立信任关系
     * 参数           描述                                                                                      是否必带
     * msg_signature	微信加密签名，msg_signature结合了企业填写的token、请求中的timestamp、nonce参数、加密的消息体	是
     * timestamp	    时间戳	                                                                                       是
     * nonce	        随机数	                                                                                       是
     * echostr	    加密的随机字符串，以msg_encrypt格式提供。需要解密并返回echostr明文，解密后有random、msg_len、msg、$CorpID四个字段，其中msg即为echostr明文	首次校验时必带
     * <p>
     * GET /cgi-bin/wxpush?
     * msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&
     * timestamp=1409659589&
     * nonce=263014780&
     * echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
     *
     * @return
     */
    @RequestMapping(value = "", produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String weixin(String msg_signature, String timestamp, String nonce, String echostr) {
        try {
            //获取接入对象
            WXBizMsgCrypt wxcpt = getWXBizMsgCrypt();
            //解码
            if (echostr != null) {
                String sEchoStr; //需要返回的明文
                sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
                return sEchoStr;
            }
        } catch (AesException e) {
            System.out.println("fail");
            return e.getMessage();
        }
        return "none";
    }
    // 微信token
    String wxToken = null;
    // 过期时间
    Long wxTokenExpireTime = 0L;
    /**
     * 获取微信token
     * url: https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx8e1c59bef7d57d5d&corpsecret=rP2a2VJjhDr0wSVzumH-cbAX9xFsbP7CIHIEBQLnsPBfCu3jzNS3I6nU53LMaucs
     *
     * @return String token
     */
    /*public String getWeixinToken() {
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
*/
    public List<String> exclodeKeywords = new ArrayList<String>() {{
        add("UNDELIV"); // 江西错误码
        add("MK:0000"); // 空号
        add("MK:0001");  // 空号
        add("MK:0012");  // 空号
        add("MN:0001");  // 空号
        add("BLACK");   // 江西黑名单
        add("KEYWORD"); // 江西关键词
        add("SIGFAIL"); // 江西签名错误
        add("BEYONDN"); // 江西错误码
        add("NOTITLE"); // 江西错误码
        add("MB:1042");  // 1631湖南通道发送超长英文短信
        add("LT:0012"); // 江西联通空号码
        add("3810691631 提交异常：13"); // 湖南通道，号码空号
        add("3810691631 提交异常：9"); // 湖南通道，携号转网
        add("MH:0007"); // 湖南通道，携号转网
    }};

    /**
     * 排除报警关键词
     *
     * @param add    添加的关键词
     * @param remove 删除的关键词
     * @return
     */
    @RequestMapping(value = "keywords", produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String keywords(String add, String remove, HttpServletRequest request) {
        String ip = getIpAddress(request);
        if ("221.178.187.2".equals(ip)) {
            if (add != null && !exclodeKeywords.contains(add)) {
                exclodeKeywords.add(add);
            }
            if (remove != null && exclodeKeywords.contains(remove)) {
                exclodeKeywords.remove(remove);
            }
        }
        return exclodeKeywords.toString().replace(",", "\n");
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

    ArrayList<String> ipList = new ArrayList<String>() {{
        //公司IP
        add("221.178.187.2");   // 移动
        add("112.25.208.103");   // 移动
        //武汉212
        add("27.24.190.212");   // 电信
        //阿里云官网
        add("112.74.215.52");   // BGP
        // 无锡一期
        add("43.254.52.253");   // BGP
        add("103.21.119.77");   // 电信
        // 无锡二期
//        add("221.228.101.194"); // 电信
        // 上海网域
        add("114.141.128.178");// BGP
        add("222.73.107.5");    // 电信
        add("114.141.128.179");// BGP
        add("222.73.107.6");    // 电信
        // 芊云
        add("47.97.113.52");    // EIP
        // 上海尔坤
        add("47.97.118.97");    //  阿里云
        // 江西
        add("47.97.118.98");    // EIP
        // 深圳联通
        add("58.250.177.142");    //  华为云
        // 恒云太
        add("58.215.112.86");    // 电信
        add("112.25.74.206");    // 移动
        add("112.25.74.254");    // 移动
        add("153.35.103.13");    // 联通
    }};

    /**
     * 报警接口
     * @param code      报警类型
     * @param content   报警内容
     * @param platform  平台标识
     * @param request
     * @return
     */
    @RequestMapping(value = "send", method = RequestMethod.POST)
    @ResponseBody
    public String postToWeixin(@RequestParam(value = "code", required = false) Integer code,
                               @RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "platform", required = false) String platform,
                               HttpServletRequest request) {
        if (code == null || content == null) {
            return "2";
        }
        String ip = getIpAddress(request);
        List<String> ipList = whiteIpService.getWhiteIps();
        // 调试模式，不限IP
        if (!debug && !ipList.contains(ip)) {
            return "4";
        }
        Map<Integer, String> codeMap;
        if ("47.97.113.52".equals(ip)) {
            // 芊云平台
            codeMap = aliCodeMap;
        } else if ("47.97.118.97".equals(ip)) {
            // 上海尔坤
            codeMap = erKunCodeMap;
        } else if ("58.250.177.142".equals(ip)) {
            // 深圳联通
            codeMap = szUnicomCodeMap;
        } else if("47.97.118.98".equals(ip)){
            codeMap = jiangxiCodeMap;
        }else{
            codeMap = defaultCodeMap;
        }
        // 匹配通讯录标签
        String tagId = codeMap.get(code);
        if (tagId == null) {
            return "6";
        }
        // 反馈状态报警忽略
        if (code == 105) {
            for (String word : exclodeKeywords) {
                if (content.contains(word)) {
                    return "5";
                }
            }
        }
        // 获取微信身份标识
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String token = SendWXserviceImpl.getWeixinToken();
        System.out.println(code + "\t" + platform + "\t" + content);
        if (token == null) {
            return "1";
        }
        try {
            HttpPost httpPost = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token);
            Message message = new Message();
            message.setTotag(tagId);
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
                        return "0;";
                    } else {
                        return retSrc;
                    }
                } else {
                    System.out.println("post httpEntity = null");
                    return "3";
                }
            } else {
                System.out.println("post status:\t" + response.getStatusLine().getStatusCode());
                return "3";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 从 request请求对象 中获取ip地址
     *
     * @param request
     * @return
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return String.valueOf(ip);
    }

    public static void main(String[] args) {
        /*WeiXinController controller = new WeiXinController();
        String token = controller.getWeixinToken();
        System.out.println(token);*/
    }
}
