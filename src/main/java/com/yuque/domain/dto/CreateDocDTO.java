package com.yuque.domain.dto;

import com.yuque.anno.MapParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description
 * @since 2023/8/1 20:47:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDocDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 标题
     */
    private String title;

    /**
     * 文档 Slug(文档的路径)
     */
    private String slug;

    /**
     * 支持 markdown、lake、html，默认为 markdown
     */
    private String format;

    /**
     * format 描述的正文内容，最大允许 5MB
     */
    private String body;

    /**
     * bool
     * false: 不开启 lake 自动转换   * 默认值
     * true: 开启 lake 自动转换
     * 当遇到报错提示“抱歉，语雀不允许通过 API 修改富文本格式文档，请到语雀进行操作。”，请尝试开启 lake 自动转换。
     * (开启时 format 必须填 markdown)
     */
    @MapParams("_force_asl")
    private Boolean forceAsl;
}
