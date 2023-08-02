package com.yuque.core.client;

import com.yuque.domain.po.UserDetailSerializer;
import com.yuque.domain.vo.ResponseVO;

import java.util.Optional;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 20:07:40
 */
public interface IUser {
    /**
     * 获取用户的基本信息
     * GET /users/:login
     * # 或
     * GET /users/:id
     *
     * @param loginOrId login: liuyangfang-hangzhou or id: 29253374
     * @return UserSerializer
     */
    Optional<ResponseVO<UserDetailSerializer>> getUserInfoByLoginOrId(String loginOrId);

    /**
     * 获取认证的用户的个人信息
     * GET /user
     *
     * @return UserSerializer
     */
    Optional<ResponseVO<UserDetailSerializer>> getUserInfo();
}
