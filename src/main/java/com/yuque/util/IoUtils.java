package com.yuque.util;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.yuque.exception.YuqueException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author 11029
 * @description 本类是对IoUtil的简单封装
 * @see IoUtil
 * @since 2023/8/2 8:32:15
 */
@Slf4j
public final class IoUtils extends IoUtil {
    private IoUtils() {
    }

    /**
     * 将字符串写入本地文件
     *
     * @param path         文件所在的绝对路径
     * @param writeContent 写入到本地文件的字符串
     */
    public static void writeToFile(String path, String writeContent) throws YuqueException {
        if (StrUtil.isEmpty(path)) {
            log.info("file path not null");
            return;
        }

        String fileName;
        boolean isAbsolutePath = FileUtil.isAbsolutePath(path);
        log.info("{} isAbsolutePath {}", path, isAbsolutePath);
        if (!isAbsolutePath) {
            throw new YuqueException("不支持相对路径的填写");
        }
        fileName = path;

        File file = FileUtils.getFile(fileName);


        writeToFile(file, writeContent);
    }

    /**
     * 将字符串写入本地文件
     *
     * @param file         相对于Resource路径
     * @param writeContent 写入到本地文件的字符串
     */
    public static void writeToFile(File file, String writeContent) throws YuqueException {
        assert file != null;
        assert StrUtil.isNotEmpty(writeContent);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
             OutputStreamWriter streamWriter = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(streamWriter)) {
            bw.write(new String(writeContent.getBytes(StandardCharsets.UTF_8)));
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
