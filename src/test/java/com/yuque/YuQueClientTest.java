package com.yuque;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.yuque.client.YuQueClient;
import com.yuque.domain.dto.CreateDocDTO;
import com.yuque.domain.po.DocSerializer;
import com.yuque.domain.po.TocSerializer;
import com.yuque.domain.vo.DownloadVO;
import com.yuque.domain.vo.ResponseListVO;
import com.yuque.exception.YuqueException;
import org.junit.Test;

import java.util.*;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 19:28:44
 */
public class YuQueClientTest {
    private static YuQueClient client = new YuQueClient("xxxxx", "G:/YuQueExportDownload");

    @Test
    public void testGetUserInfoByLoginOrId() throws Exception {
        client.getUserInfoByLoginOrId("liuyangfang-hangzhou").ifPresent(userVo -> System.out.println(userVo.getData().toString()));
    }

    @Test
    public void testGetUserInfo() throws Exception {
        client.getUserInfo().ifPresent(userVo -> System.out.println(userVo.getData()));
    }

    @Test
    public void testGetReposByNameSpaceOrId() throws Exception {
        client.getDocList("28511144").ifPresent(System.out::println);
    }

    @Test
    public void testGetDoc() throws Exception {
        client.getDoc("28511144", "plhxfw").ifPresent(System.out::println);
    }

    @Test
    public void testCreateDoc() throws Exception {
        CreateDocDTO dto = CreateDocDTO.builder().title("DocDetailSerializer").slug(UUID.randomUUID().toString(true).substring(0, 10)).body("文档详细信息\n" +
                "webhook消息里带的信息如下：\n" +
                "\n" +
                "## Attributes\n" +
                "\n" +
                "- id - 文档编号\n" +
                "- slug - 文档路径\n" +
                "- title - 标题\n" +
                "- book_id - 仓库编号，就是 repo_id\n" +
                "- book - 仓库信息 <[BookSerializer](BookSerializer)>，就是 repo 信息\n" +
                "- user_id - 用户/团队编号\n" +
                "- user - 用户/团队信息 <[UserSerializer](UserSerializer)>\n" +
                "- format - 描述了正文的格式 `[lake , markdown]` \n" +
                "- body - 正文 Markdown 源代码\n" +
                "- body_draft - 草稿 Markdown 源代码\n" +
                "- **body_html - 转换过后的正文 HTML （重大变更，详情请参考：**[https://www.yuque.com/yuque/developer/yr938f](https://www.yuque.com/yuque/developer/yr938f)**）**\n" +
                "- creator_id - 文档创建人 [User](UserSerializer) Id\n" +
                "- public - 公开级别 [0 - 私密, 1 - 公开]\n" +
                "- status - 状态 [0 - 草稿, 1 - 发布]\n" +
                "- likes_count - 赞数量\n" +
                "- comments_count - 评论数量\n" +
                "- content_updated_at - 文档内容更新时间\n" +
                "- deleted_at - 删除时间，未删除为 `null` \n" +
                "- created_at - 创建时间\n" +
                "- updated_at - 更新时间\n").format("markdown").build();

        client.createDoc("28511144", dto).ifPresent(System.out::println);
    }


    @Test
    public void testUpdateDoc() throws Exception {
        CreateDocDTO dto = CreateDocDTO.builder()
                .title("DocDetailSerializer")
                .slug("twosegsoxtetzv0p")
                .body("文档详细信息\n" +
                        "webhook消息里带的信息如下：\n" +
                        "\n" +
                        "## Attributes\n" +
                        "\n" +
                        "- id - 文档编号\n" +
                        "- slug - 文档路径\n" +
                        "- title - 标题\n" +
                        "- book_id - 仓库编号，就是 repo_id\n" +
                        "- book - 仓库信息 <[BookSerializer](BookSerializer)>，就是 repo 信息\n" +
                        "- user_id - 用户/团队编号\n" +
                        "- user - 用户/团队信息 <[UserSerializer](UserSerializer)>\n" +
                        "- format - 描述了正文的格式 `[lake , markdown]` \n" +
                        "- body - 正文 Markdown 源代码\n" +
                        "- body_draft - 草稿 Markdown 源代码\n" +
                        "- **body_html - 转换过后的正文 HTML （重大变更，详情请参考：**[https://www.yuque.com/yuque/developer/yr938f](https://www.yuque.com/yuque/developer/yr938f)**）**\n" +
                        "- creator_id - 文档创建人 [User](UserSerializer) Id\n" +
                        "- public - 公开级别 [0 - 私密, 1 - 公开]\n" +
                        "- status - 状态 [0 - 草稿, 1 - 发布]\n" +
                        "- likes_count - 赞数量\n" +
                        "- comments_count - 评论数量\n" +
                        "- content_updated_at - 文档内容更新时间\n" +
                        "- deleted_at - 删除时间，未删除为 `null` \n" +
                        "- created_at - 创建时间\n" +
                        "- updated_at - 更新时间\n")
                .format("markdown")
                .forceAsl(Boolean.FALSE)
                .build();

        client.updateDoc("28511144", "135029233", dto).ifPresent(System.out::println);
    }

    @Test
    public void testDeleteDoc() throws Exception {
        client.deleteDoc("28511144", "135029233").ifPresent(System.out::println);
    }

    @Test
    public void testDownloadMarkdown() throws Exception {
        client.download("28511144", "ol8g8k");
    }

    @Test
    public void testSearch() throws Exception {
        client.search("java").ifPresent(System.out::println);
    }

    @Test
    public void testGetDocList() throws Exception {
        Optional<ResponseListVO<TocSerializer>> listVOOptional = client.getRepoTocs("28511144");
        listVOOptional.ifPresent(docSerializerResponseListVO -> {
            List<TocSerializer> data = docSerializerResponseListVO.getData();
            for (TocSerializer datum : data) {
                System.out.println(datum);
            }
        });
    }

    @Test
    public void testGetDownloadVoList() throws Exception {
        List<DownloadVO> list = client.getDownloadVOListByRepoIdOrNamespace("41109689");
        if (CollectionUtil.isNotEmpty(list)) {
            for (DownloadVO downloadVO : list) {
                System.out.println(downloadVO.getTitle() + "-" + downloadVO.getSlug());
            }
        }
    }


    @Test
    public void testBatchDownloadByRepoNamespaceOrId() throws Exception {
        client.batchDownload("41109689");
    }
}
