package com.yuque.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description
 * @since 2023/8/2 9:50:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRepoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Book, Design, all - 所有类型
     */
    private String type;

    /**
     * 用于分页，效果类似 MySQL 的 limit offset，一页 20 条
     */
    private Integer offset;
}
