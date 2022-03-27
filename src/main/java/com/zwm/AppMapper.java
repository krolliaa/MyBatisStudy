package com.zwm;

import com.zwm.mapper.UserMapper;
import com.zwm.pojo.User;
import com.zwm.util.MyBatisUtil;

import java.util.List;

public class AppMapper {
    public static void main(String[] args) {
        UserMapper userMapper = MyBatisUtil.getSqlSession().getMapper(UserMapper.class);
        List<User> userList = userMapper.selectAllUsers();
        userList.forEach(user -> System.out.println(user.toString()));
        User user = userMapper.selectUserById(1);
        System.out.println(user.toString());
    }
}
