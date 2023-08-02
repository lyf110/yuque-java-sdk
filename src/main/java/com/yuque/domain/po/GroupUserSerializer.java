package com.yuque.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author 11029
 * @description 语雀：https://www.yuque.com/yuque/developer/groupuserserializer
 * @since 2023/8/1 18:23:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUserSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // GroupUser Id
    private String group_id; // 团队编号
    private UserSerializer group; // 团队信息 <UserSerializer>
    private String user_id; // 用户编号
    private UserSerializer user; // 用户信息 <UserSerializer>
    private String role; // 角色 [0 - Owner, 1 - Member]
    private String created_at; // 创建时间
    private String updated_at; // 更新时间
}
