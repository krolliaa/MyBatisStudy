package com.kk;

import com.kk.mapper.CacheMapper;
import com.kk.pojo.Emp;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        SqlSession sqlSession1 = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper = sqlSession.getMapper(CacheMapper.class);
        CacheMapper cacheMapper1 = sqlSession1.getMapper(CacheMapper.class);
        Emp emp1 = cacheMapper.selectEmpById(1);
        System.out.println(emp1);
        Emp emp2 = cacheMapper1.selectEmpById(1);
        System.out.println(emp2);
    }
}
