package com.yuque.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.yuque.domain.po.BookSerializer;
import com.yuque.domain.po.UserSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 11029
 * @description
 * @since 2023/8/2 12:26:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTargetVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id; // 仓库编号
    private String type; // 类型 [Book - 文档]
    private String slug; // 仓库路径
    private String description; // 介绍
    private String user_id; // 所属的团队/用户编号
    private String book_id;
    private String format;
    @JSONField(name = "public")
    private String public_; // 公开状态 [1 - 公开, 0 - 私密]
    private Integer view_status;
    private Integer read_status;
    private String likes_count; // 喜欢数量
    private Integer read_count;
    private Integer comments_count;
    private String content_updated_at;
    private String created_at; // 创建时间
    private String updated_at; // 更新时间
    private String published_at;
    private String first_published_at;
    private Integer draft_version;
    private Integer last_editor_id;
    private Integer word_count;
    private String cover;
    private String custom_cover;
    private String custom_description;
    private Integer hits;
    private String last_editor;
    private BookSerializer book;
    private UserSerializer user;
}
