package com.yuque.core.client;

import com.yuque.domain.vo.DownloadVO;
import com.yuque.exception.YuqueException;

import java.util.List;

/**
 * @author 11029
 * @description 下载接口
 * @since 2023/8/2 8:21:14
 */
public interface IDownload {
    /**
     * 下载文档
     *
     * @param nameSpaceOrId 知识库命名空间或者知识库id
     * @param slug          文档的路径
     */
    void download(String nameSpaceOrId, String slug);

    /**
     * 下载文档
     *
     * @param downloadVO 下载文档的基础信息封装
     */
    void download(DownloadVO downloadVO);

    /**
     * 批量下载文档，默认不启用多线程下载
     *
     * @param downloadVOList 批量下载文档的基础信息封装
     */
    void batchDownload(List<DownloadVO> downloadVOList);

    /**
     * 批量下载文档
     *
     * @param downloadVOList 批量下载文档的基础信息封装
     * @param isMultiThread  是否启用多线程下载
     */
    void batchDownload(List<DownloadVO> downloadVOList, boolean isMultiThread);

    /**
     * 下载整个知识库所有内容，并保留其目录结构
     *
     * @param nameSpaceOrId 知识库命名空间或者id
     */
    void batchDownload(String nameSpaceOrId) throws YuqueException;
}
