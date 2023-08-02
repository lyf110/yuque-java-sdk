package com.yuque.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 11029
 * @description 语雀实体类 https://www.yuque.com/yuque/developer/userserializer
 * @since 2023/8/1 17:20:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSerializer implements Serializable {
    private static final long serialVersionUID = 1L;


    private String id; // 用户编号
    private String type; // 类型 [`User`  - 用户, Group - 团队]
    private String login; // 用户个人路径
    private String name; // 昵称

    private String description; // 介绍
    private String avatar_url; // 头像 URL
    private LocalDateTime created_at; // 创建时间
    private LocalDateTime updated_at; // 更新时间
}
