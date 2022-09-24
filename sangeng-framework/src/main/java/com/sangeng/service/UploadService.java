package com.sangeng.service;

import com.sangeng.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ZayneChen
 * @date 2022年09月12日 11:22
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
