package com.kk;

import com.kk.mapper.UserMapper;
import com.kk.pojo.User;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAge(100);
        user.setSex("女");
        user.setEmail("admin@qq.com");
        int result = userMapper.insertUserGeneratedKeys(user);
        System.out.println(result);
        System.out.println(user);
    }
}
