package com.yuque.client;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yuque.client.base.IYuQueClient;
import com.yuque.client.http.HttpClientBase;
import com.yuque.domain.dto.*;
import com.yuque.domain.po.*;
import com.yuque.domain.vo.DownloadVO;
import com.yuque.domain.vo.ResponseListVO;
import com.yuque.domain.vo.ResponseVO;
import com.yuque.domain.vo.SearchVO;
import com.yuque.exception.YuqueException;
import com.yuque.util.IoUtils;
import com.yuque.util.function.MySupplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.*;
import java.util.concurrent.*;

import static com.yuque.constants.YuQueConstants.getRestUrl;
import static com.yuque.util.PoUtil.toMap;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 19:16:58
 */
@Slf4j
public class YuQueClient extends HttpClientBase implements IYuQueClient {
    @Getter
    @Setter
    private ExecutorService executorService;

    public YuQueClient(String yuQueToken, String saveBaseDirs) {
        super(yuQueToken, saveBaseDirs);
    }

    public YuQueClient(String yuQueToken, String saveBaseDirs, ExecutorService executorService) {
        super(yuQueToken, saveBaseDirs);
        if (executorService == null) {
            initBaseExecutorService();
        }
    }

    /**
     * 提供默认的线程池
     */
    public void initBaseExecutorService() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = corePoolSize * 2;
        executorService = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadFactoryBuilder()
                        .setThreadFactory(Executors.defaultThreadFactory())
                        .setNameFormat("download-thread-%d")
                        .build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Override
    public Optional<ResponseVO<UserDetailSerializer>> getUserInfoByLoginOrId(String loginOrId) {
        assert StrUtil.isNotEmpty(loginOrId);
        return requestHandler(() -> buildHttpGet(getRestUrl("/users/" + loginOrId), null),
                new TypeReference<ResponseVO<UserDetailSerializer>>() {
                });
    }

    @Override
    public Optional<ResponseVO<UserDetailSerializer>> getUserInfo() {
        return requestHandler(() -> buildHttpGet(getRestUrl("/user"), null),
                new TypeReference<ResponseVO<UserDetailSerializer>>() {
                });
    }

    @Override
    public Optional<ResponseListVO<DocSerializer>> getDocList(String namespaceOrId) {
        assert StrUtil.isNotEmpty(namespaceOrId);
        return requestHandler(() -> buildHttpGet(getRestUrl("/repos/" + namespaceOrId + "/docs"), null),
                new TypeReference<ResponseListVO<DocSerializer>>() {
                });
    }

    @Override
    public Optional<ResponseVO<DocDetailSerializer>> getDoc(String namespace, String slug) {
        assert StrUtil.isNotEmpty(namespace);
        assert StrUtil.isNotEmpty(slug);
        Map<String, String> params = new HashMap<>(1);
        params.put("raw", "1");

        return requestHandler(() -> buildHttpGet(getRestUrl("/repos/" + namespace + "/docs/" + slug), params),
                new TypeReference<ResponseVO<DocDetailSerializer>>() {
                });
    }

    @Override
    public Optional<ResponseVO<DocDetailSerializer>> createDoc(String namespaceOrId, CreateDocDTO docDTO) {
        assert StrUtil.isNotEmpty(namespaceOrId);
        assert docDTO != null;
        return requestHandler(() -> buildHttpPost(getRestUrl("/repos/" + namespaceOrId + "/docs"), toMap(docDTO)),
                new TypeReference<ResponseVO<DocDetailSerializer>>() {
                });
    }

    @Override
    public Optional<ResponseVO<DocDetailSerializer>> updateDoc(String namespaceOrId, String docId, CreateDocDTO docDTO) {
        // PUT /repos/:namespace/docs/:id
        assert StrUtil.isNotEmpty(namespaceOrId);
        assert StrUtil.isNotEmpty(docId);
        assert docDTO != null;

        return requestHandler(() -> buildHttpPut(getRestUrl("/repos/" + namespaceOrId + "/docs/" + docId), toMap(docDTO)),
                new TypeReference<ResponseVO<DocDetailSerializer>>() {
                });
    }

    @Override
    public Optional<ResponseVO<DocDetailSerializer>> deleteDoc(String namespaceOrId, String docId) {
        // DELETE /repos/:namespace/docs/:id
        assert StrUtil.isNotEmpty(namespaceOrId);
        assert StrUtil.isNotEmpty(docId);
        return requestHandler(() -> buildHttpDelete(getRestUrl("/repos/" + namespaceOrId + "/docs/" + docId), null),
                new TypeReference<ResponseVO<DocDetailSerializer>>() {
                });
    }

    @Override
    public Optional<DownloadVO> getMarkdownBody(String nameSpaceOrId, String slug) {
        Optional<ResponseVO<DocDetailSerializer>> docOptional = getDoc(nameSpaceOrId, slug);
        if (!docOptional.isPresent()) {
            return Optional.empty();
        }
        ResponseVO<DocDetailSerializer> docDetailVO = docOptional.get();
        DocDetailSerializer docDetail = docDetailVO.getData();
        if (docDetail != null) {
            String body = docDetail.getBody();
            if (StrUtil.isEmpty(body)) {
                return Optional.empty();
            }

            return Optional.of(DownloadVO.of(body.replaceAll(REGEX, "").replaceAll(BR_REGEX, System.lineSeparator()),
                    docDetail.getTitle(),
                    docDetail.getSlug()));

        }

        return Optional.empty();
    }

    @Override
    public void download(String nameSpaceOrId, String slug) {
        getMarkdownBody(nameSpaceOrId, slug).ifPresent(this::download);
    }

    @Override
    public void download(DownloadVO downloadVO) {
        if (downloadVO == null) {
            log.warn("下载文档的实体信息为空，不能下载");
            return;
        }
        log.info("{} download start", downloadVO.getTitle());
        try {
            String path = saveBaseDirs + FILE_SEPARATOR + downloadVO.getTitle() + MD_SUFFIX;
            IoUtils.writeToFile(path, downloadVO.getBody());
        } catch (YuqueException e) {
            e.printStackTrace();
        }
        log.info("{} download end", downloadVO.getTitle());
    }

    @Override
    public void batchDownload(List<DownloadVO> downloadVOList) {
        batchDownload(downloadVOList, false);
    }

    @Override
    public void batchDownload(List<DownloadVO> downloadVOList, boolean isMultiThread) {
        assert CollectionUtil.isNotEmpty(downloadVOList);
        if (isMultiThread) {
            if (executorService == null) {
                initBaseExecutorService();
            }

            CountDownLatch countDownLatch = new CountDownLatch(downloadVOList.size());

            try {
                downloadVOList.forEach(downloadVO -> executorService.submit(() -> {
                    download(downloadVO);
                    countDownLatch.countDown();
                }));

                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭线程池
                executorService.shutdown();
            }
        } else {
            downloadVOList.forEach(this::download);
        }
    }

    @Override
    public void batchDownload(String nameSpaceOrId) throws YuqueException {
        List<DownloadVO> list = getDownloadVOListByRepoIdOrNamespace(nameSpaceOrId);
        if (CollectionUtil.isEmpty(list)) {
            log.warn("知识库: {}, 不存在文档，请检查", nameSpaceOrId);
            return;
        }

        batchDownload(list, true);
    }

    private <R> Optional<R> requestHandler(MySupplier<HttpRequestBase> supplier,
                                           TypeReference<R> typeReference) {
        try {
            String json = doRequest(supplier.get());
            if (StrUtil.isEmpty(json)) {
                return Optional.empty();
            }

            R result = JSONObject.parseObject(json, typeReference);
            return Optional.ofNullable(result);
        } catch (YuqueException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResponseListVO<SearchVO>> search(SearchDTO searchDTO) throws YuqueException {
        return requestHandler(() -> buildHttpGet(getRestUrl("/search"), toMap(searchDTO)),
                new TypeReference<ResponseListVO<SearchVO>>() {
                }
        );
    }

    @Override
    public Optional<ResponseListVO<SearchVO>> search(String queryStr) throws YuqueException {
        return search(SearchDTO.builder()
                .type("doc")
                .q(queryStr)
                .build());
    }

    @Override
    public List<DownloadVO> getDownloadVOListByRepoIdOrNamespace(String repoIdOrNamespace) throws YuqueException {
        Optional<ResponseListVO<TocSerializer>> voOptional = getRepoTocs(repoIdOrNamespace);
        if (!voOptional.isPresent()) {
            return Collections.emptyList();
        }


        ResponseListVO<TocSerializer> listVO = voOptional.get();
        List<TocSerializer> tocSerializerList = listVO.getData();
        if (CollectionUtil.isEmpty(tocSerializerList)) {
            return Collections.emptyList();
        }

        Map<String, String> map = new HashMap<>();
        List<DownloadVO> downloadVOList = new ArrayList<>();

        for (TocSerializer tocSerializer : tocSerializerList) {
            String uuid = tocSerializer.getUuid();
            String title = "/" + tocSerializer.getTitle();
            String parent_uuid = tocSerializer.getParent_uuid();
            String type = tocSerializer.getType();
            String url = tocSerializer.getUrl();
            if (StrUtil.isNotEmpty(parent_uuid)) {
                title = map.get(parent_uuid) + title;
            }

            if ("doc".equalsIgnoreCase(type)) {
                String finalTitle = title;
                getMarkdownBody(repoIdOrNamespace, url).ifPresent(downloadVO ->
                        downloadVOList.add(DownloadVO.of(downloadVO.getBody(), finalTitle, url)));

            }

            map.put(uuid, title);
        }

        return downloadVOList;
    }

    /**
     * 获取某个用户的加入的团队列表
     * GET /users/:login/groups
     * # 或
     * GET /users/:id/groups
     *
     * @param loginOrId 用户id或者login
     * @return 组织列表 Array<UserSerializer>
     */
    @Override
    public Optional<ResponseListVO<UserSerializer>> listJoinedGroupByLoginOrId(String loginOrId) {
        assert StrUtil.isNotEmpty(loginOrId);
        return requestHandler(() -> buildHttpGet(getRestUrl("/users/" + loginOrId + "/groups"), null),
                new TypeReference<ResponseListVO<UserSerializer>>() {
                });
    }

    /**
     * 获取团队成员信息
     * <p>
     * GET /groups/:login/users
     * # 或
     * GET /groups/:id/users
     *
     * @param loginOrId    成员login或者id
     * @param groupUserDTO 成员基本信息
     * @return 团队成员信息 Array<GroupUserSerializer>
     */
    @Override
    public Optional<ResponseListVO<GroupUserSerializer>> listGroupUsers(String loginOrId,
                                                                        GroupUserDTO groupUserDTO) {
        assert StrUtil.isNotEmpty(loginOrId);
        assert groupUserDTO != null;
        return requestHandler(() -> buildHttpGet(getRestUrl("/groups/" + loginOrId + "/users"), toMap(groupUserDTO)),
                new TypeReference<ResponseListVO<GroupUserSerializer>>() {
                });
    }

    /**
     * 增加或更新团队成员
     * PUT /groups/:group_login/users/:login
     * # 或
     * PUT /groups/:group_id/users/:login
     * </p>
     * 需要 abilities.group_user.read 权限
     * 需要有 abilities.group_user.create 权限
     * role 为 0 的时候，需要 abilities.group_user.update 权限
     *
     * @param groupLoginOrGroupId 组织login或者组织id
     * @param login               成员login
     * @param groupUserDTO        团队成员信息
     * @return 团队成员信息
     */
    @Override
    public Optional<ResponseVO<GroupUserSerializer>> addGroupUser(String groupLoginOrGroupId, String login, GroupUserDTO groupUserDTO) {
        return requestHandler(() -> buildHttpPut(getRestUrl("/groups/" + groupLoginOrGroupId + "/users/" + login),
                        toMap(groupUserDTO)),
                new TypeReference<ResponseVO<GroupUserSerializer>>() {
                });
    }

    /**
     * 删除团队成员
     * DELETE /groups/:group_login/users/:login
     * # 或
     * DELETE /groups/:group_id/users/:login
     * 需要有 abilities.group_user.destroy 权限
     *
     * @param groupLoginOrGroupId 组织login或者组织id
     * @param login               成员login
     * @apiNote 此外，不可以自己删除自己。写方法时最好校验下
     */
    @Override
    public void deleteGroupUser(String groupLoginOrGroupId, String login) {
        assert StrUtil.isNotEmpty(groupLoginOrGroupId);
        assert StrUtil.isNotEmpty(login);
        try {
            doRequest(buildHttpDelete(getRestUrl("/groups/" + groupLoginOrGroupId + "/users/" + login), null));
        } catch (YuqueException e) {
            e.printStackTrace();
        }
    }

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
    @Override
    public Optional<ResponseListVO<BookSerializer>> listUserRepos(String loginOrId, GetRepoDTO repoDTO) {
        assert StrUtil.isNotEmpty(loginOrId);
        assert repoDTO != null;
        return requestHandler(() -> buildHttpGet(getRestUrl("/users/" + loginOrId + "/repos"), toMap(repoDTO)),
                new TypeReference<ResponseListVO<BookSerializer>>() {
                });
    }

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
    @Override
    public Optional<ResponseListVO<BookSerializer>> listGroupRepos(String loginOrId, GetRepoDTO repoDTO) {
        assert StrUtil.isNotEmpty(loginOrId);
        assert repoDTO != null;
        return requestHandler(() -> buildHttpGet(getRestUrl("/groups/" + loginOrId + "/repos"), toMap(repoDTO)),
                new TypeReference<ResponseListVO<BookSerializer>>() {
                });
    }

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
    @Override
    public Optional<ResponseVO<BookDetailSerializer>> createRepoByUser(String loginOrId, CreateRepoDTO repoDTO) {
        assert StrUtil.isNotEmpty(loginOrId);
        assert repoDTO != null;
        return requestHandler(() -> buildHttpPost(getRestUrl("/users/" + loginOrId + "/repos"), toMap(repoDTO)),
                new TypeReference<ResponseVO<BookDetailSerializer>>() {
                });
    }

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
    @Override
    public Optional<ResponseVO<BookDetailSerializer>> createRepoByGroup(String loginOrId, CreateRepoDTO repoDTO) {
        assert StrUtil.isNotEmpty(loginOrId);
        assert repoDTO != null;
        return requestHandler(() -> buildHttpPost(getRestUrl("/groups/" + loginOrId + "/repos"), toMap(repoDTO)),
                new TypeReference<ResponseVO<BookDetailSerializer>>() {
                });
    }

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
    @Override
    public Optional<ResponseVO<BookDetailSerializer>> getRepoDetail(String namespaceOrId, GetRepoDTO repoDTO) {
        assert StrUtil.isNotEmpty(namespaceOrId);
        assert repoDTO != null;
        return requestHandler(() -> buildHttpGet(getRestUrl("/repos/" + namespaceOrId), toMap(repoDTO)),
                new TypeReference<ResponseVO<BookDetailSerializer>>() {
                });
    }

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
    @Override
    public Optional<ResponseVO<BookDetailSerializer>> updateRepo(String namespaceOrId, CreateRepoDTO repoDTO) {
        assert StrUtil.isNotEmpty(namespaceOrId);
        assert repoDTO != null;
        return requestHandler(() -> buildHttpPut(getRestUrl("/repos/" + namespaceOrId), toMap(repoDTO)),
                new TypeReference<ResponseVO<BookDetailSerializer>>() {
                });
    }

    /**
     * 删除知识库
     * DELETE /repos/:namespace
     * # 或
     * DELETE /repos/:id
     * 需要 Repo 的 abilities.destroy 权限
     *
     * @param namespaceOrId 知识库的命名空间或者id
     */
    @Override
    public void deleteRepo(String namespaceOrId) {
        assert StrUtil.isNotEmpty(namespaceOrId);
        try {
            doRequest(buildHttpDelete(getRestUrl("/repos/" + namespaceOrId), null));
        } catch (YuqueException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定知识库的目录列表
     * GET /repos/:namespace/toc
     * or
     * GET /repos/:id/toc
     *
     * @param namespaceOrId namespace or id
     * @return 知识库的目录列表
     */
    @Override
    public Optional<ResponseListVO<TocSerializer>> getRepoTocs(String namespaceOrId) {
        return requestHandler(() -> buildHttpGet(getRestUrl("/repos/" + namespaceOrId + "/toc"), null),
                new TypeReference<ResponseListVO<TocSerializer>>() {
                });
    }
}
