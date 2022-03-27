package com.zwm.mapper;

import com.zwm.pojo.User;

import java.util.List;

public interface UserMapper {
    public abstract User selectUserById(int id);
    public abstract List<User> selectAllUsers();
}
