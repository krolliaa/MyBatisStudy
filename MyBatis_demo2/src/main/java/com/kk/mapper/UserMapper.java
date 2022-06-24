package com.kk.mapper;

import com.kk.pojo.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    public abstract User selectUserByUsername(String username);

    public abstract User loginCheck(String username, String password);

    public abstract User loginCheckByMap(Map map);

    public abstract int insertUser(User user);

    public abstract User loginCheckByParam(@Param(value = "param1") String username, @Param(value = "param2") String password);

    @MapKey(value = "id")
    public abstract Map<String, Object> selectUsersToMap();

    public abstract User selectUserByI(String username);

    public abstract int deleteMore(String usernames);

    public abstract int insertUserGeneratedKeys(User user);
}
