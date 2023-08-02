package com.yuque.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description 语雀：https://www.yuque.com/yuque/developer/userdetailserializer
 * @since 2023/8/1 17:44:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; //用户资料编号
    private String space_id; // 企业空间编号

    private String account_id; // 用户账户编号

    private String type; // 类型 [User - 用户, Group - 团队]

    private String login; // 用户个人路径

    private String name; // 昵称

    private String owner_id; // 团队创建人，仅适用于 type - 'Group'

    private String avatar_url; // 头像 URL

    private String books_count; // 仓库数量

    private String public_books_count; // 公开仓库数量

    private String members_count; // 团队成员数量

    private String description; // 介绍

    private String created_at; // 创建时间

    private String updated_at; // 更新时间

}
