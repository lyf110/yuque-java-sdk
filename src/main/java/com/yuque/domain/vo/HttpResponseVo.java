package com.yuque.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * http响应接驳实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponseVo {
    private Integer httpCode;
    private String httpStatusLine;
    private String httpContent;
}