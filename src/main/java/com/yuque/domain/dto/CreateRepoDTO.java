package com.yuque.domain.dto;

import com.yuque.anno.MapParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;


/**
 * @author 11029
 * @description
 * @since 2023/8/2 9:56:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRepoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 仓库名称
     */
    private String name;

    /**
     * slug
     */
    private String slug;

    /**
     * 在更新知识库时，会有此参数
     * 更新文档仓库的目录信息
     */
    private String toc;

    /**
     * 说明
     */
    private String description;

    /**
     * 0 私密
     * 1 所有人可见
     * 2 空间成员可见
     */
    @MapParams(value = "public")
    private Integer publicType;

    /**
     * ‘Book’ 文库, ‘Design’ 画板, 请注意大小写
     */
    private String type;
}
