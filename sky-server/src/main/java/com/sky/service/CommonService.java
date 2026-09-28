package com.sky.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 通用业务接口
 */
public interface CommonService {

    /**
     * 文件上传
     *
     * @param file
     * @return 文件在OSS上的访问路径
     */
    String upload(MultipartFile file);
}
