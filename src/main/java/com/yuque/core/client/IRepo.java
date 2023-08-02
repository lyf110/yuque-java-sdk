package com.yuque.core.client;

import com.yuque.domain.dto.CreateRepoDTO;
import com.yuque.domain.dto.GetRepoDTO;
import com.yuque.domain.po.BookDetailSerializer;
import com.yuque.domain.po.BookSerializer;
import com.yuque.domain.po.TocSerializer;
import com.yuque.domain.vo.ResponseListVO;
import com.yuque.domain.vo.ResponseVO;

import java.util.Optional;

/**
 * @author 11029
 * @description 知识库操作相关api
 * @since 2023/8/2 9:47:53
 */
public interface IRepo {
    /**
     * 获取某个用户/团队的知识库列表
     * # for User
     * GET /users/:login/repos
     * <p>
     * # 或
     * GET /users/:id/repos
     *
     * @param loginOrId 用户的login或者id
     * @param repoDTO   repoDTO
     * @return 用户的知识库列表
     */
    Optional<ResponseListVO<BookSerializer>> listUserRepos(String loginOrId, GetRepoDTO repoDTO);

    /**
     * 获取某个用户/团队的知识库列表
     * # for Group
     * GET /groups/:login/repos
     * <p>
     * # 或
     * GET /groups/:id/repos
     *
     * @param loginOrId 用户的login或者id
     * @param repoDTO   repoDTO
     * @return 用户的知识库列表
     */
    Optional<ResponseListVO<BookSerializer>> listGroupRepos(String loginOrId, GetRepoDTO repoDTO);

    /**
     * 创建知识库
     * # 往自己下面创建知识库
     * POST /users/:login/repos
     * # 或
     * POST /users/:id/repos
     *
     * @param loginOrId 个人用户的login或者id
     * @param repoDTO   repoDTO
     * @return 知识库详情
     */
    Optional<ResponseVO<BookDetailSerializer>> createRepoByUser(String loginOrId, CreateRepoDTO repoDTO);

    /**
     * 创建知识库
     * # 往团队创建知识库
     * POST /groups/:login/repos
     * # 或
     * POST /groups/:id/repos
     *
     * @param loginOrId 团队的login或者id
     * @param repoDTO   repoDTO
     * @return 知识库详情
     */
    Optional<ResponseVO<BookDetailSerializer>> createRepoByGroup(String loginOrId, CreateRepoDTO repoDTO);

    /**
     * 获取知识库详情
     * GET /repos/:namespace
     * # 或
     * GET /repos/:id
     *
     * @param namespaceOrId 知识库的命名空间或者id
     * @param repoDTO       repoDTO（type: 仓库类型，Book - 文库，Design - 设计稿）
     * @return 知识库详情
     */
    Optional<ResponseVO<BookDetailSerializer>> getRepoDetail(String namespaceOrId, GetRepoDTO repoDTO);

    /**
     * 更新知识库信息
     * PUT /repos/:namespace
     * # 或
     * PUT /repos/:id
     * 需要 Repo 的 abilities.update 权限
     *
     * @param namespaceOrId 知识库的命名空间或者id
     * @param repoDTO       知识库基本信息
     * @return 知识库详情
     */
    Optional<ResponseVO<BookDetailSerializer>> updateRepo(String namespaceOrId, CreateRepoDTO repoDTO);

    /**
     * 删除知识库
     * DELETE /repos/:namespace
     * # 或
     * DELETE /repos/:id
     * 需要 Repo 的 abilities.destroy 权限
     *
     * @param namespaceOrId 知识库的命名空间或者id
     */
    void deleteRepo(String namespaceOrId);

    /**
     * 获取指定知识库的目录列表
     * GET /repos/:namespace/toc
     * or
     * GET /repos/:id/toc
     *
     * @param namespaceOrId namespace or id
     * @return 知识库的目录列表
     */
    Optional<ResponseListVO<TocSerializer>> getRepoTocs(String namespaceOrId);
}
