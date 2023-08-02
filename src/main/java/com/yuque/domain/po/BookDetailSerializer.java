package com.yuque.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description 语雀：https://www.yuque.com/yuque/developer/bookdetailserializer
 * @since 2023/8/1 18:21:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDetailSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // 仓库编号
    private String type; // 类型 [Book - 文档]
    private String slug; // 仓库路径
    private String name; // 名称
    private String namespace; // 仓库完整路径 user.login/book.slug
    private String user_id; // 所属的团队/用户编号
    private UserSerializer user; // <UserSerializer>
    private String description; // 介绍
    private String toc_yml; // 目录原文
    private String creator_id; // 创建人 User Id
    @JSONField(name = "public")
    private String public_; // 公开状态 [1 - 公开, 0 - 私密]
    private String items_count; // 文档数量
    private String likes_count; // 喜欢数量
    private String watches_count; // 订阅数量
    private String created_at; // 创建时间
    private String updated_at; // 更新时间
}
