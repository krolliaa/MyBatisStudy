package com.zwm.mapper;

import com.zwm.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    public abstract User selectUserById(int id);
    public abstract List<User> selectAllUsers();
    public abstract User selectUserByUserName(@Param("myName") String username);
    public abstract int insertUser(User user);
    public abstract User selectUserByIdAndPassword(int id, String password);
}
