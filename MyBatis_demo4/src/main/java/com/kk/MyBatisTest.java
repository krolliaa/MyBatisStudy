package com.kk;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kk.mapper.EmpMapper;
import com.kk.pojo.Emp;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
        //pageNum：当前页的页码
        //pageSize：每页显示的条数
        //index = (pageNum - 1) * pageSize;
        PageHelper.startPage(2, 2);
        List<Emp> empList = empMapper.selectByExample(null);
        empList.forEach(System.out::println);
        PageInfo pageInfo = new PageInfo(empList, 5);//用于指定导航框中的显示个数
        System.out.println(pageInfo);
    }
}
