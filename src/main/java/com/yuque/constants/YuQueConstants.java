package com.yuque.constants;

import cn.hutool.core.util.StrUtil;

/**
 * @author 11029
 * @description 语雀常量类
 * 语雀开发者文档：https://www.yuque.com/yuque/developer
 * @since 2023/8/1 19:11:05
 */
public interface YuQueConstants {
    /**
     * 语雀API的基础路径
     */
    String BASE_API = "https://www.yuque.com/api/v2";
    String BASE_API_TEMPLATE = "https://www.yuque.com/api/v2{}";

    /**
     * 所有语雀接口都必须携带此请求头
     */
    String TOKEN_HEADER_KEY = "X-Auth-Token";

    /**
     * https://www.yuque.com/yuque/developer/user
     * 获取单个用户信息
     * GET /users/:login
     * # 或
     * GET /users/:id
     */
    String GET_USER_API_TEMPLATE = "/users/{}";

    /**
     * https://www.yuque.com/yuque/developer/user
     * 获取认证的用户的个人信息
     */
    String GET_USER_API = "/user";

    /**
     * GET /users/:login/groups
     * # 或
     * GET /users/:id/groups
     * 获取某个用户的加入的团队列表
     */
    String GET_GROUP_USERS_API_TEMPLATE = "/users/{}/groups";

    /**
     * 获取团队成员信息
     * GET /groups/:login/users
     * # 或
     * GET /groups/:id/users
     */
    String GET_GROUP_USER_API_TEMPLATE = "/groups/{}/users";

    /**
     * 增加或更新团队成员
     * <p>
     * PUT /groups/:group_login/users/:login
     * # 或
     * PUT /groups/:group_id/users/:login
     */
    String PUT_GROUPS_USER_API_TEMPLATE = "/groups/{}/users/{}";

    /**
     * 删除团队成员
     * DELETE /groups/:group_login/users/:login
     * # 或
     * DELETE /groups/:group_id/users/:login
     */
    String DELETE_GROUPS_USER_API_TEMPLATE = "/groups/{}/users/{}";

    /**
     * 获取某个用户/团队的知识库列表
     * # for User
     * GET /users/:login/repos
     * <p>
     * # 或
     * GET /users/:id/repos
     */
    String GET_USER_REPOS_API_TEMPLATE = "/users/{}/repos";

    /**
     * 获取某个用户/团队的知识库列表
     * # for Group
     * GET /groups/:login/repos
     * <p>
     * # 或
     * GET /groups/:id/repos
     */
    String GET_GROUP_REPOS_API_TEMPLATE = "/groups/{}/repos";

    /**
     * 创建知识库
     * # 往团队创建知识库
     * POST /groups/:login/repos
     * # 或
     * POST /groups/:id/repos
     */
    String POST_GROUPS_REPOS_API_TEMPLATE = "/groups/{}/repos";

    /**
     * 创建知识库
     * # 往自己下面创建知识库
     * POST /users/:login/repos
     * # 或
     * POST /users/:id/repos
     */
    String POST_USERS_REPOS_API_TEMPLATE = "/users/{}/repos";

    /**
     * 获取知识库详情
     * GET /repos/:namespace
     * # 或
     * GET /repos/:id
     */
    String GET_REPOS_API_TEMPLATE = "/repos/{}";

    /**
     * 更新知识库信息
     * PUT /repos/:namespace
     * # 或
     * PUT /repos/:id
     */
    String PUT_REPOS_API_TEMPLATE = "/repos/{}";

    /**
     * 删除知识库
     * DELETE /repos/:namespace
     * # 或
     * DELETE /repos/:id
     */
    String DELETE_REPOS_API_TEMPLATE = "/repos/{}";


    /**
     * 获取一个仓库的文档列表
     * GET /repos/:namespace/docs
     * # 或
     * GET /repos/:id/docs
     */
    String GET_REPOS_DOCS_API_TEMPLATE = "/repos/{}/docs";


    /**
     * 获取单篇文档的详细信息
     * 暂不支持获取表格、画板和数据表类型文档的内容。
     * <p>
     * GET /repos/:namespace/docs/:slug
     */
    String GET_REPOS_NAMESPACE_DOCS_SLUG_API_TEMPLATE = "/repos/{}/docs/{}";


    /**
     * 获取指定知识库的目录列表
     * GET /repos/:namespace/toc
     * or
     * GET /repos/:id/toc
     */
    String GET_REPOS_TOC_API_TEMPLATE = "/repos/{}/toc";


    /**
     * 创建文档
     * <p>
     * POST /repos/:namespace/docs
     * # 或
     * POST /repos/:id/docs
     */
    String POST_REPOS_DOCS_API_TEMPLATE = "/repos/{}/docs";

    /**
     * 更新文档
     * <p>
     * PUT /repos/:namespace/docs/:id
     * # 或
     * PUT /repos/:repo_id/docs/:id
     */
    String PUT_REPOS_DOCS_API_TEMPLATE = "/repos/{}/docs/{}";

    /**
     * 删除文档
     * <p>
     * DELETE /repos/:namespace/docs/:id
     * # 或
     * DELETE /repos/:repo_id/docs/:id
     */
    String DELETE_REPOS_DOCS_API_TEMPLATE = "/repos/{}/docs/{}";

    /**
     * GET /search
     */
    String SEARCH_API = "/search";

    /**
     * 获取请求的url
     *
     * @param template 目标方法
     * @param params   参数
     * @return 请求的url
     */
    static String getRestApi(String template, Object... params) {
        if (params == null || params.length == 0) {
            return template;
        }
        return getAndAddBaseApi(StrUtil.format(template, params));
    }

    /**
     * 获取rest请求的全路径
     *
     * @param originUrl 原始URL
     * @return rest请求的全路径
     */
    static String getAndAddBaseApi(String originUrl) {
        return getUrlByTemplateAndOriginUrl(BASE_API_TEMPLATE, originUrl);
    }

    static String getUrlByTemplateAndOriginUrl(String template, String originUrl) {
        return StrUtil.format(template, originUrl);
    }
}
