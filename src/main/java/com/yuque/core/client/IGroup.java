package com.yuque.core.client;

import com.yuque.domain.dto.GroupUserDTO;
import com.yuque.domain.po.GroupUserSerializer;
import com.yuque.domain.po.UserSerializer;
import com.yuque.domain.vo.ResponseListVO;
import com.yuque.domain.vo.ResponseVO;
import com.yuque.exception.YuqueException;

import java.util.Optional;

/**
 * @author 11029
 * @description
 * @since 2023/8/2 9:14:38
 */
public interface IGroup {
    /**
     * 获取某个用户的加入的团队列表
     * GET /users/:login/groups
     * # 或
     * GET /users/:id/groups
     *
     * @param loginOrId 用户id或者login
     * @return 组织列表 Array<UserSerializer>
     * @throws YuqueException YuqueException
     */
    Optional<ResponseListVO<UserSerializer>> listJoinedGroupByLoginOrId(String loginOrId) throws YuqueException;

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
     * @throws YuqueException YuqueException
     */
    Optional<ResponseListVO<GroupUserSerializer>> listGroupUsers(String loginOrId, GroupUserDTO groupUserDTO) throws YuqueException;

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
    Optional<ResponseVO<GroupUserSerializer>> addGroupUser(String groupLoginOrGroupId, String login, GroupUserDTO groupUserDTO);

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
    void deleteGroupUser(String groupLoginOrGroupId, String login);
}
