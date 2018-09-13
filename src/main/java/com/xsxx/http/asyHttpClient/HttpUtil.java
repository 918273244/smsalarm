package com.xsxx.http.asyHttpClient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpUtil {

    private static int socketTimeout = 1000;// 设置等待数据超时时间5秒钟 根据业务调整

    private static int connectTimeout = 2000;// 连接超时

    private static int poolSize = 100;// 连接池最大连接数

    private static int maxPerRoute = 10;// 每个主机的并发最多10


    /**
     * 创建非异步的可关闭的且跳过https 验证的 httpClient
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    /**
     * 创建异步的可关闭的跳过https验证的 httpClient对象
     * @param connManager  连接管理器  可以调用本类的getConnManager 生成
     * @return
     */
    public static CloseableHttpAsyncClient getClient(PoolingNHttpClientConnectionManager connManager) {
        if (null == connManager) {
            return null;
        }
        // 设置连接参数
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpUtil.socketTimeout).setConnectTimeout(HttpUtil.connectTimeout).build();
        // 创建自定义的httpclient对象
        CloseableHttpAsyncClient client = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connManager).disableCookieManagement().build();
        return client;
    }

    /**
	 * 创建异步的可关闭的跳过https验证的 httpClient对象(绑定本地网卡)
	 * @param connManager
	 * @param localAddress
	 * @return
	 * @throws
	 */
	public static CloseableHttpAsyncClient getClient(PoolingNHttpClientConnectionManager connManager, String localAddress) throws UnknownHostException {
		if (null == connManager || null == localAddress) {
			return null;
		}
		String[] ipStrArr = localAddress.split("\\.");
		if(ipStrArr.length != 4){
			return null;
		}
		byte[] ip = new byte[]{ (byte)(Integer.parseInt(ipStrArr[0])), (byte) (Integer.parseInt(ipStrArr[1])), (byte) (Integer.parseInt(ipStrArr[2])), (byte) (Integer.parseInt(ipStrArr[3])) };

		// 设置连接参数
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpUtil.socketTimeout).setConnectTimeout(HttpUtil.connectTimeout).setLocalAddress(InetAddress.getByAddress(ip)).build();
		// 创建自定义的httpclient对象
		CloseableHttpAsyncClient client = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connManager).disableCookieManagement().build();
		return client;
	}

    /**
     * 初始化 连接管理器
     * @return
     */
    public static PoolingNHttpClientConnectionManager getConnManager() {

        try {
            // 绕过证书验证，处理https请求
            SSLContext sslcontext = createIgnoreVerifySSL();

            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder
                    .<SchemeIOSessionStrategy> create().register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", new SSLIOSessionStrategy(sslcontext, new AllowAllHostnameVerifier())).build();
            // 配置io线程
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setConnectTimeout(HttpUtil.socketTimeout).setSoTimeout(HttpUtil.connectTimeout)
                    .setRcvBufSize(8192).setSndBufSize(8192).build();

            // 设置连接池大小
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor, null,
                    sessionStrategyRegistry, null);
            connManager.setDefaultMaxPerRoute(maxPerRoute);
            connManager.setMaxTotal(poolSize);
            return connManager;
        } catch (IOReactorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[] { trustManager }, null);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sc;

    }

    /**
     * 从 request请求对象 中获取ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        if(request==null){
            return null;
        }
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
        return ip;
    }
}
