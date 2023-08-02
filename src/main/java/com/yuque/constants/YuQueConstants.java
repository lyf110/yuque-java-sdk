package com.yuque.constants;

import cn.hutool.core.util.StrUtil;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 19:11:05
 */
public interface YuQueConstants {
    /**
     * 语雀API的基础路径
     */
    String BASE_API = "https://www.yuque.com/api/v2";
    String BASE_API_TEMPLATE = "https://www.yuque.com/api/v2{}";
    String TOKEN_HEADER_KEY = "X-Auth-Token";

    /**
     * 获取rest请求的全路径
     *
     * @param originUrl 原始URL
     * @return rest请求的全路径
     */
    static String getRestUrl(String originUrl) {
        return getUrlByTemplateAndOriginUrl(BASE_API_TEMPLATE, originUrl);
    }

    static String getUrlByTemplateAndOriginUrl(String template, String originUrl) {
        return StrUtil.format(template, originUrl);
    }
}
