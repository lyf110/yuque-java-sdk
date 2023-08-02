package com.yuque.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description 语雀：https://www.yuque.com/yuque/developer/docdetailserializer
 * @since 2023/8/1 18:22:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocDetailSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // 文档编号
    private String slug; // 文档路径
    private String title; // 标题
    private String book_id; // 仓库编号，就是 repo_id
    private BookSerializer book; // 仓库信息 <BookSerializer>，就是 repo 信息
    private String user_id; // 用户/团队编号
    private UserSerializer user; // 用户/团队信息 <UserSerializer>
    private String format; // 描述了正文的格式 [lake , markdown]
    private String body; // 正文 Markdown 源代码
    private String body_draft; // 草稿 Markdown 源代码
    private String body_html; // 转换过后的正文 HTML （重大变更，详情请参考：https://www.yuque.com/yuque/developer/yr938f）
    private String creator_id; // 文档创建人 User Id
    @JSONField(name = "public")
    private String public_; // 公开级别 [0 - 私密, 1 - 公开]
    private String status; // 状态 [0 - 草稿, 1 - 发布]
    private String likes_count; // 赞数量
    private String comments_count; // 评论数量
    private String content_updated_at; // 文档内容更新时间
    private String deleted_at; // 删除时间，未删除为 null
    private String created_at; // 创建时间
    private String updated_at; // 更新时间
}
