package com.yuque.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description 语雀：https://www.yuque.com/yuque/developer/docversiondetailserializer
 * @since 2023/8/1 18:22:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocVersionDetailSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // 草稿编号
    private String doc_id; // 文档编号
    private String slug; // 文档 slug
    private String title; // 标题
    private String user_id; // 创建人编号
    private UserSerializer user; // 创建人 <UserSerializer>
    private String draft; // 是否是草稿
    private String body; // 正文 markdown，参考 <DocDetailSerializer> 里面关于 body, body_asl 的介绍
    private String body_asl; // asl 格式正文
    private String body_html; // 正文 HTML 代码
    private String created_at; // 创建时间
    private String updated_at; // 更新时间
}
