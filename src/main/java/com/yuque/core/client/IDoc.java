package com.yuque.core.client;

import com.yuque.domain.dto.CreateDocDTO;
import com.yuque.domain.po.DocDetailSerializer;
import com.yuque.domain.po.DocSerializer;
import com.yuque.domain.vo.DownloadVO;
import com.yuque.domain.vo.ResponseListVO;
import com.yuque.domain.vo.ResponseVO;

import java.util.Optional;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 20:08:19
 */
public interface IDoc {
    /**
     * 根据命名空间和或者知识库id，获取一个仓库的文档列表
     * GET /repos/:namespace/docs
     * # 或
     * GET /repos/:id/docs
     * <p>
     * 需要 Repo 的 abilities.read 权限
     * <p>
     * 如果是 Group 成员，将能获取到私密文档、未发布的草稿。
     * <p>
     * 支持传递 offset / limit 进行分页获取。
     * <p>
     * 支持传递 optional_properties=hits 获取文档浏览数。
     *
     * @param namespaceOrId namespace or repo_id
     * @return 仓库的文档列表
     */
    Optional<ResponseListVO<DocSerializer>> getDocList(String namespaceOrId);

    /**
     * 获取单篇文档的详细信息
     * GET /repos/:namespace/docs/:slug
     *
     * @param namespace 命名空间
     * @param slug      文档的路径
     * @return DocDetailSerializer
     */
    Optional<ResponseVO<DocDetailSerializer>> getDoc(String namespace, String slug);

    /**
     * 创建文档
     * POST /repos/:namespace/docs
     * # 或
     * POST /repos/:id/docs
     * <br/>
     * 需要 Repo 的 abilities.doc.create 权限
     *
     * @param namespaceOrId namespace or id
     * @param docDTO        docDTO
     * @return 文档详情
     */
    Optional<ResponseVO<DocDetailSerializer>> createDoc(String namespaceOrId, CreateDocDTO docDTO);


    /**
     * 更新文档
     * PUT /repos/:namespace/docs/:id
     * # 或
     * PUT /repos/:repo_id/docs/:id
     * <br/>
     * 需要 Doc 的 abilities.update 权限
     * 注意! 这里最后个参数是 id （文档编号）而不是 slug，原因是为了避免 slug 改变无法正确保存。
     *
     * @param namespaceOrId namespace or id
     * @param docId         文档id
     * @param docDTO        docDTO
     * @return 文档详情
     */
    Optional<ResponseVO<DocDetailSerializer>> updateDoc(String namespaceOrId, String docId, CreateDocDTO docDTO);


    /**
     * 删除文档
     * DELETE /repos/:namespace/docs/:id
     * # 或
     * DELETE /repos/:repo_id/docs/:id
     * <br/>
     * 需要 Doc 的 abilities.destroy 权限
     * 注意! 这里最后个参数是 id （文档编号）而不是 slug，原因是为了避免 slug 改变无法正确保存。
     *
     * @param namespaceOrId namespace or id
     * @param docId         文档id
     * @return 文档详情
     */
    Optional<ResponseVO<DocDetailSerializer>> deleteDoc(String namespaceOrId, String docId);


    /**
     * 获取文档的markdown格式内容
     *
     * @param nameSpaceOrId 知识库命名空间或者知识库id
     * @param slug          文档的路径
     */
    Optional<DownloadVO> getMarkdownBody(String nameSpaceOrId, String slug);
}
