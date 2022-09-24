package com.sangeng.service;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.User;

/**
 * @author ZayneChen
 * @date 2022年09月03日 16:30
 */
public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
