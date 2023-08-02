package com.yuque.client.base;

import com.yuque.domain.dto.SearchDTO;
import com.yuque.domain.po.BookDetailSerializer;
import com.yuque.domain.vo.DownloadVO;
import com.yuque.domain.vo.ResponseListVO;
import com.yuque.domain.vo.SearchVO;
import com.yuque.exception.YuqueException;

import java.util.List;
import java.util.Optional;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 20:00:49
 */
public interface IYuQueClient extends IUser, IDoc, IGroup, IRepo, IDownload {

    /**
     * 搜索
     * 可以搜索自己有权限的内容，包括：
     * 1、【空间】搜索空间下公开的内容
     * 2、搜索我有权限的团队或者知识库下的内容
     * 3、搜索与我相关的内容
     *
     * @param searchDTO 搜索的基本参数
     * @return 文档基本详情
     */
    Optional<ResponseListVO<SearchVO>> search(SearchDTO searchDTO) throws YuqueException;

    /**
     * 根据关键字进行基本查询
     *
     * @param queryStr 关键词
     * @return 文档基本详情
     */
    Optional<ResponseListVO<SearchVO>> search(String queryStr) throws YuqueException;


    /**
     * 获取一个知识库下面的下载信息，用于批量下载实现
     *
     * @param repoIdOrNamespace repoIdOrNamespace
     * @return 封装的下载实体类
     * @throws YuqueException YuqueException
     */
    List<DownloadVO> getDownloadVOListByRepoIdOrNamespace(String repoIdOrNamespace) throws YuqueException;
}
