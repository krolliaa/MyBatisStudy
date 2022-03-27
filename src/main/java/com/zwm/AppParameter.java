package com.zwm;

import com.zwm.mapper.UserMapper;
import com.zwm.pojo.User;
import com.zwm.util.MyBatisUtil;

import java.util.HashMap;
import java.util.Map;

public class AppParameter {
    /*
        MyBatis 可以传递单个参数可以直接传，格式为：#{属性名}，属性可以是基本数据类型也可以是 String 类型
        MyBatis 也可以传递多个参数：
        1. 以对象的形式传递多个参数，可以直接使用 #{属性名} 的形式传递，前提是该对象的类属性名和数据库名保持一直
           那如果不保持一致该如何修改呢？此时就要修改 UserMapper.xml 中的 resultType 改为 resultMap 这个后面会学习到
        2. 以 @Param 的形式传递 ---> User selectUserByUsername(@Param(‘myName) String username);
        3. 以位置形式传递参数，格式如：#{arg0}, #{arg1}
    */
    public static void main(String[] args) {
        UserMapper userMapper = MyBatisUtil.getSqlSession().getMapper(UserMapper.class);
        User user = userMapper.selectUserByUserName("AA");
        System.out.println(user.toString());
        System.out.println(userMapper.insertUser(new User(11, "AK", "123456")));
        user = userMapper.selectUserByIdAndPassword(1, "123456");
        System.out.println(user.toString());
        Map<String, Object> data = new HashMap<>();
        data.put("myName", "AA");
        data.put("myPassword", "123456");
        user = userMapper.selectUserByUsernameAndPassword(data);
        System.out.println(user.toString());
    }
}
