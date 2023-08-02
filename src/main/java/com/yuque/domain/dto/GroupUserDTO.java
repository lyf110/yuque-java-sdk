package com.yuque.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 11029
 * @description
 * @since 2023/8/2 9:29:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 非必须
     * 0：管理员
     * 1：成员
     */
    private Integer role;

    /**
     * 偏移量，分页时使用，非必须
     * 默认为0
     * 从第几条开始取数据
     * 每次固定返回 100 个
     */
    private Integer offset;
}
