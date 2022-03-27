package com.zwm.mapper;

import com.zwm.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    public abstract User selectUserById(int id);
    public abstract List<User> selectAllUsers();
    public abstract User selectUserByUserName(@Param("myName") String username);
    public abstract int insertUser(User user);
    public abstract User selectUserByIdAndPassword(int id, String password);
    public abstract User selectUserByUsernameAndPassword(Map<String, Object> data);
}
