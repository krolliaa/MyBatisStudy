package com.zwm.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory = null;

    static {
        try {
            String mybatisConfig = "mybatis.xml";
            InputStream inputStream = Resources.getResourceAsStream(mybatisConfig);
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
        } catch (IOException e) {
            sqlSessionFactory = null;
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        SqlSession sqlSession = null;
        if (sqlSessionFactory != null) {
            return sqlSessionFactory.openSession();
        } else {
            return null;
        }
    }
}