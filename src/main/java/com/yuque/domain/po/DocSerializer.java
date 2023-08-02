package com.yuque.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description 语雀：https://www.yuque.com/yuque/developer/docserializer
 * @since 2023/8/1 18:22:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // 文档编号
    private String slug; // 文档路径
    private String title; // 标题
    private String user_id; // 文档创建人 user_id
    private String format; // 描述了正文的格式 [asl, markdown]

    @JSONField(name = "public")
    private String public_; // 是否公开 [1 - 公开, 0 - 私密]
    private String status; // 状态 [1 - 正常, 0 - 草稿]
    private String likes_count; // 喜欢数量
    private String comments_count; // 评论数量
    private String content_updated_at; // 文档内容更新时间
    private BookSerializer book; // <BookSerializer> 所属知识库
    private UserSerializer user; // <UserSerializer> 所属团队（个人）
    private UserSerializer last_editor; // <UserSerializer> 最后修改人
    private String created_at; // 创建时间
    private String updated_at; // 更新时间
}
