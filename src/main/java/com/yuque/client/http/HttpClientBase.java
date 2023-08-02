package com.yuque.client.http;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yuque.exception.YuqueException;
import com.yuque.util.function.MyFunction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author 11029
 * @description HTTPClient工具类
 * @since 2023/8/1 23:00:01
 */
public class HttpClientBase {
    /**
     * 连接超时时间
     */
    public static final int CONNECTION_TIMEOUT_MS = 5000;
    /**
     * 读取数据超时时间
     */
    public static final int SO_TIMEOUT_MS = 5000;

    /**
     * utf8编码
     */
    public static final String utf8 = StandardCharsets.UTF_8.toString();

    /**
     * 默认的空字符串
     */
    public static final String EMPTY_STR = "";

    /**
     * 时间格式化模板
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    /**
     * 去除语雀文档中的<a name="dsdfsdfsd-sd"></a>标签的正则匹配表达式
     */
    public static final String REGEX = "<a name=\"[a-zA-Z0-9\\-_]+.\"></a>";

    /**
     * 将语雀中的换行符转成\r\n
     */
    public static final String BR_REGEX = "<br />";

    /**
     * 路径分隔符
     */
    public static final String FILE_SEPARATOR = "/";

    public static final String MD_SUFFIX = ".md";

    protected final String yuqueToken;
    protected final String saveBaseDirs;

    public HttpClientBase(String yuqueToken, String saveBaseDirs) {
        assert StrUtil.isNotEmpty(yuqueToken);
        assert StrUtil.isNotEmpty(saveBaseDirs);
        this.yuqueToken = yuqueToken;
        if (saveBaseDirs.endsWith(FILE_SEPARATOR) || saveBaseDirs.endsWith("\\")) {
            this.saveBaseDirs = saveBaseDirs + LocalDateTime.now().format(FORMATTER);
        } else {
            this.saveBaseDirs = saveBaseDirs + FILE_SEPARATOR + LocalDateTime.now().format(FORMATTER);
        }
    }

    /**
     * 构建GET请求方法
     *
     * @param uri    请求的路径
     * @param params 额外参数
     * @return GET请求方法
     * @throws YuqueException YuqueException
     */
    public HttpGet buildHttpGet(String uri, Map<String, String> params) throws YuqueException {
        return buildBaseHttp(uri, params, uriBuilder -> new HttpGet(uriBuilder.build()));
    }

    /**
     * 构建POST请求方法
     *
     * @param uri    请求的路径
     * @param params 额外参数
     * @return POST请求方法
     * @throws YuqueException YuqueException
     */
    public HttpPost buildHttpPost(String uri, Map<String, String> params) throws YuqueException {
        return buildBaseHttp(uri, params, uriBuilder -> new HttpPost(uriBuilder.build()));
    }

    /**
     * 构建PUT请求方法
     *
     * @param uri    请求的路径
     * @param params 额外参数
     * @return PUT请求方法
     * @throws YuqueException YuqueException
     */
    public HttpPut buildHttpPut(String uri, Map<String, String> params) throws YuqueException {
        return buildBaseHttp(uri, params, uriBuilder -> new HttpPut(uriBuilder.build()));
    }

    /**
     * 构建DELETE请求方法
     *
     * @param uri    请求的路径
     * @param params 额外参数
     * @return DELETE请求方法
     * @throws YuqueException YuqueException
     */
    public HttpDelete buildHttpDelete(String uri, Map<String, String> params) throws YuqueException {
        return buildBaseHttp(uri, params, uriBuilder -> new HttpDelete(uriBuilder.build()));
    }

    /**
     * 抽取的构建请求的通用方法
     *
     * @param uri      请求的路径
     * @param params   额外参数
     * @param function 具体构建请求方法的函数
     * @param <R>      具体的请求方法，如get，post，put，delete等方法
     * @return 具体的请求方法，如get，post，put，delete等方法
     * @throws YuqueException YuqueException
     */
    private <R> R buildBaseHttp(String uri, Map<String, String> params, MyFunction<URIBuilder, R> function)
            throws YuqueException {
        R httpRequestBase;
        try {
            URIBuilder builder = new URIBuilder(uri);
            if (CollectionUtil.isNotEmpty(params)) {
                params.forEach(builder::setParameter);
            }
            httpRequestBase = function.apply(builder);
        } catch (URISyntaxException e) {
            throw new YuqueException(e.getMessage(), e);
        }
        return httpRequestBase;
    }

    /**
     * 创建HttpClient
     *
     * @param isMultiThread 是否为多线程
     * @return HttpClient
     */
    public HttpClient buildHttpClient(boolean isMultiThread) {
        CloseableHttpClient client;
        if (isMultiThread)
            client = HttpClientBuilder
                    .create().setDefaultRequestConfig(buildRequestConfig(null))
                    .setRetryHandler(new DefaultHttpRequestRetryHandler())
                    .setConnectionManager(
                            new PoolingHttpClientConnectionManager()).build();
        else {
            client = HttpClientBuilder.create().setDefaultRequestConfig(buildRequestConfig(null)).build();
        }
        return client;
    }

    /**
     * 构建公用RequestConfig
     *
     * @return RequestConfig
     */
    public RequestConfig buildRequestConfig(HttpHost proxy) {
        // 设置请求和传输超时时间
        return RequestConfig.custom().setProxy(proxy)
                .setCookieSpec(CookieSpecs.STANDARD)
                .setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
    }

    /**
     * 发送http请求
     *
     * @param httpMethod Http请求方法
     * @return http响应结果
     */
    public String doRequest(HttpRequestBase httpMethod) throws YuqueException {
        //设置权限头信息
        httpMethod.setHeader("X-Auth-Token", this.yuqueToken);
        HttpClient httpClient = buildHttpClient(true);

        try {
            HttpResponse response = httpClient.execute(httpMethod);
            assertStatus(response);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, utf8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 强验证必须是200状态否则报异常
     *
     * @param response 响应结果
     * @throws YuqueException YuqueException
     */
    private void assertStatus(HttpResponse response) throws YuqueException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            try {
                String entity = EntityUtils.toString(response.getEntity(), utf8);
                throw YuqueException.builder()
                        .origMsg(entity)
                        .httpCode(statusCode)
                        .origStatus(statusCode)
                        .build();
            } catch (IOException e) {
                throw new YuqueException(e.getMessage(), e);
            }
        }
    }
}
