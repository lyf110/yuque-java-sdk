package com.yuque.domain.dto;

import lombok.*;

import java.io.Serializable;


/**
 * @author 11029
 * @description
 * @since 2023/8/2 10:46:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源类型：
     * repo - 知识库
     * doc - 文档
     */
    @NonNull
    private String type;

    /**
     * 关键字 (UriEncoded)
     */
    @NonNull
    private String q;

    /**
     * 分页，1、2...
     */
    private Integer offset;

    /**
     * 搜索路径
     * 团队或者知识库 url 上的路径
     * <p>
     * 例如 scope=lark 搜索 lark 组的内容，scope=lark/help 搜索 lark/help 知识库下的内容
     */
    private String scope;

    /**
     * 搜索与我相关的内容
     */
    private String related;
}
