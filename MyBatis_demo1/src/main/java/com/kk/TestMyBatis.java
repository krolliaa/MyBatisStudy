package com.kk;

import com.kk.mapper.UserMapper;
import com.kk.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class TestMyBatis {
    public static void main(String[] args) {
        InputStream inputStream = null;
        SqlSession sqlSession = null;
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
            sqlSession = sqlSessionFactory.openSession(true);//自动提交事务
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //添加操作
            String result = userMapper.insertUser() == 1 ? "成功" : "失败";
            System.out.println("添加" + result);
            //修改操作
            result = userMapper.updateUser() == 1 ? "成功" : "失败";
            System.out.println("修改" + result);
            //查询操作
            User user = userMapper.selectUserById();
            System.out.println("查询结果：" + user);
            //删除操作
            result = userMapper.deleteUser() == 1 ? "成功" : "失败";
            System.out.println("删除" + result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sqlSession != null) sqlSession.close();
        }
    }
}
