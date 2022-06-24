package com.kk.mapper;

import com.kk.pojo.User;

public interface UserMapper {
    /**
     * 添加用户信息
     */
    public abstract int insertUser();

    /**
     * 删除用户信息
     */
    public abstract int deleteUser();

    /**
     * 修改用户信息
     */
    public abstract int updateUser();

    /**
     * 查询用户信息
     */
    public abstract User selectUserById();
}