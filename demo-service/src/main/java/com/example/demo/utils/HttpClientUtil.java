package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * 封装了采用HttpClient发送HTTP请求的方法
 *
 * @Author: fg
 * @Date: 2020/11/20
 */
@Slf4j
public class HttpClientUtil {

    private static final Integer HTTP_TIMEOUT = 60 * 1000;
    private static String DEFAULT_RESULT = "{resCode:\"9999\",resMsg:\"通信失败\"}";

    private HttpClientUtil() {

    }

    /**
     * 发送HTTP_GET请求
     *
     * @param reqURL        请求地址(含参数)
     * @param encodeCharset 编码方式：(URF-8)
     * @return 远程主机响应正文
     * @see 1)该方法会自动关闭连接,释放资源
     * @see 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * @see 4)该方法会自动获取到响应消息头中[Content-Type:text/html; charset=GBK]的charset值作为响应报文的解码字符集
     * @see 5)若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     */
    public static String sendHttpGetRequest(String reqURL, String encodeCharset) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 设置请求和传输超时时间（版本>=4.3设置超时方式）
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(30000).build();

        HttpGet httpGet = new HttpGet(reqURL);
        httpGet.setConfig(requestConfig);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 获取响应实体
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                DEFAULT_RESULT = EntityUtils.toString(entity, encodeCharset);
                EntityUtils.consume(entity);
            }

            log.info("-------------------------------------------------------------------------------------------");
            log.info("请求地址：" + reqURL);
            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            log.info("HTTP应答完整报文");
            // HTTP应答状态行信息
            log.info(response.getStatusLine().toString());
            // HTTP应答报文头信息
            log.info(respHeaderDatas.toString().trim());
            // HTTP应答报文体信息
            log.info(DEFAULT_RESULT);
            log.info("-------------------------------------------------------------------------------------------");

        } catch (ParseException pe) {
            log.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", pe);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            if (httpClient != null) {
                // 关闭连接,释放资源
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        return DEFAULT_RESULT;
    }

    /**
     * 发送HTTP_POST请求
     *
     * @param reqURL        请求地址
     * @param reqData       请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
     * @param encodeCharset 编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
     * @param contentType   请求方式
     * @return 远程主机响应正文
     * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
     * @see 2)该方法会自动关闭连接,释放资源
     * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
     * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html; charset=GBK]的charset值
     * @see 6)若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
     */
    public static String sendHttpPostRequest(String reqURL, String reqData, String encodeCharset, String contentType) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 设置请求和传输超时时间（版本>=4.3设置超时方式）
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2400000).setConnectTimeout(2400000).build();

        HttpPost httpPost = new HttpPost(reqURL);

        httpPost.setConfig(requestConfig);

        // 这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息
        httpPost.setHeader(HTTP.CONTENT_TYPE, contentType + ";charset=" + encodeCharset);

        try {
            httpPost.setEntity(new StringEntity(reqData == null ? "" : reqData, encodeCharset));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                DEFAULT_RESULT = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }

            log.info("-------------------------------------------------------------------------------------------");
            log.info("请求地址：" + reqURL);
            log.info("请求参数：" + reqData);
            StringBuilder respHeaderDatas = new StringBuilder();
            for (Header header : response.getAllHeaders()) {
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            log.info("HTTP应答完整报文");
            // HTTP应答状态行信息
            log.info(response.getStatusLine().toString());
            // HTTP应答报文头信息
            log.info(respHeaderDatas.toString().trim());
            // HTTP应答报文体信息
            log.info(DEFAULT_RESULT);
            log.info("-------------------------------------------------------------------------------------------");

        } catch (ConnectTimeoutException cte) {
            log.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
        } catch (SocketTimeoutException ste) {
            log.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            if (httpClient != null) {
                // 关闭连接,释放资源
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        return DEFAULT_RESULT;
    }

    /**
     * 生成表单请求的参数字符串
     *
     * @param params params
     * @return String
     */
    public static String generateFormString(List<BasicNameValuePair> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (BasicNameValuePair param : params) {
            sb.append(param.getName()).append("=").append(param.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

}
