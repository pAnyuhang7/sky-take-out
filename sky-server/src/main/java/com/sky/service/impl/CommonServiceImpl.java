package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用业务实现类
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        try {
            //1、获取原始文件名，如：abc.jpg
            String originalFilename = file.getOriginalFilename();

            //2、截取原始文件名的后缀，如：.jpg
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            //3、用UUID生成新的文件名（避免不同用户上传的文件重名覆盖）
            String objectName = UUID.randomUUID().toString() + extension;

            //4、上传文件到阿里云OSS，返回文件的访问路径
            return aliOssUtil.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
            throw new BaseException(MessageConstant.UPLOAD_FAILED);
        }
    }
}
