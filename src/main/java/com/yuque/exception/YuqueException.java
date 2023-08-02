package com.yuque.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YuqueException extends Exception {
    private static final long serialVersionUID = 2214381471513460742L;


    /**
     * 返回的http code
     */
    private Integer httpCode;
    /**
     * 返回的http 响应头 那一行
     */
    private String httpStatusLine;
    /**
     * 返回的http body
     */
    private String origContent;
    /**
     * 自定义error
     */
    private String customErrorMsg;
    /**
     * 语雀返回的原始json->message
     */
    private String origMsg;
    /**
     * 语雀返回的原始json->code
     */
    private String origCode;
    /**
     * 语雀返回的原始json->status
     */
    private Integer origStatus;

    public YuqueException(String message, Throwable e) {
        super(message, e);
        this.customErrorMsg = message;
    }

    public YuqueException(String message) {
        super(message);
        this.customErrorMsg = message;
    }
}
