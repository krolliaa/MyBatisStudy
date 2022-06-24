# `MyBatis`

# 2022年06月22日下午

`MyBatis`的原名叫做`iBatis`，`3.x`版本之后菜更名为`MyBatis`。`MyBatis`是一个半自动的对象关系映射框架。

## `MyBatis_demo1`创建

- 直接点`Maven`创建，`ctrl + shift + s`打开设置页面，点击`Maven`修改`Maven home path`，注意如果是刚下载的`Maven`，记得修改下`settings.xml`的`repository`仓库地址

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>MyBatisStudy</artifactId>
          <groupId>com.zwm</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
      <artifactId>MyBatis_demo1</artifactId>
      <packaging>jar</packaging>
  
      <properties>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <maven.compiler.source>1.8</maven.compiler.source>
          <maven.compiler.target>1.8</maven.compiler.target>
      </properties>
  
      <dependencies>
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>8.0.28</version>
          </dependency>
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis</artifactId>
              <version>3.5.3</version>
          </dependency>
      </dependencies>
  
      <build>
          <resources>
              <resource>
                  <directory>src/main/java</directory>
                  <includes>
                      <include>**/*.properties</include>
                      <include>**/*.xml</include>
                  </includes>
                  <filtering>false</filtering>
              </resource>
          </resources>
      </build>
  </project>
  ```

## `MyBatis`核心配置文件

创建`MyBatis`的核心配置文件

- 习惯命名：`mybatis-config.xml`

- 配置数据库连接环境

  - `transactionManage`标签：`JDBC/MANAGED`
    - `JDBC`：表示当前环境中执行`SQL`使用的是`JDBC`原生的管理方式，事物的提交和回滚需要手动的`commit/rollback`
    - `MANAGED`：被管理，例如`Spring`
  - `dataSource`标签：`POOLED/UNPOOLED/JNDI`
    - `POOLED`：表示使用数据库连接池缓存数据库连接
    - `UNPOOLED`：表示不适用数据库连接池
    - `JNDI`：表示使用上下文中的数据源
  - `properties`标签：
    - 为了避免`properties`键名重复，可以加个前缀比如：`jdbc.driver + jdbc.url`等
    - `<properties resource="jdbc.properties"></properties>`

  ```xml
  <!--配置连接数据库的环境-->
  <environments default="mysql">
      <environment id="mysql">
          <transactionManager type="JDBC"></transactionManager>
          <dataSource type="POOLED">
              <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
              <property name="url" value="jdbc:mysql://localhost:3306/mybatis_study?useSSL=false&amp;serverTimezone=Asia/Shanghai"/>
              <property name="username" value="root"/>
              <property name="password" value="123456"/>
          </dataSource>
      </environment>
  </environments>
  ```

- 配置映射文件

  ​	**【即：两个一致】**

  - 如果以包为单位引入映射文件，路径需要和`mapper`接口的路径保持一致
  - 其次就是`mapper`映射文件要和`mapper`接口名保持一致

  **<font color="red">这里注意下：如果在`resources`包下创建文件使用的是：`com.kk.mapper`的方式则创建的就是一个名为：`com.kk.mapper`的文件夹，所以如果在`resources`文件夹下创建则需要使用：`com/kk/mapper`格式。</font>**

  ```xml
  <!--引入映射文件-->
  <mappers>
      <mapper resource="mappers/UserMapper.xml"></mapper>
  </mappers>
  
  <!--引入映射文件-->
  <mappers>
      <package name="com.kk.mapper"/>
  </mappers>
  ```

- 配置`properties`文件：

  ```properties
  jdbc.driver=com.mysql.cj.jdbc.Driver
  jdbc.url=jdbc:mysql://localhost:3306/mybatis_study?useSSL=false&serverTimezone=Asia/Shanghai
  jdbc.username=root
  jdbc.password=123456
  ```

  ```java
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <properties resource="jdbc.properties"></properties>
      <!--配置连接数据库的环境-->
      <environments default="mysql">
          <environment id="mysql">
              <transactionManager type="JDBC"></transactionManager>
              <dataSource type="POOLED">
                  <property name="driver" value="${jdbc.driver}"/>
                  <property name="url" value="${jdbc.url}"/>
                  <property name="username" value="${jdbc.username}"/>
                  <property name="password" value="${jdbc.password}"/>
              </dataSource>
          </environment>
      </environments>
      <!--引入映射文件-->
      <mappers>
          <mapper resource="mappers/UserMapper.xml"></mapper>
      </mappers>
  </configuration>
  ```

- 配置类型别名`typeAliases`标签

  **<font color="red">`mybatis-config.xml`核心配置文件的标签的位置是有顺序的。</font>**【三种方式】

  ```xml
  <!--配置别名-->
  <typeAliases>
      <typeAlias type="com.kk.pojo.User" alias="user"></typeAlias>
      <typeAlias type="com.kk.pojo.User"></typeAlias>
      <package name="com.kk.pojo"></package>
  </typeAliases>
  ```

## `MyBatis`映射文件

`MyBatis`的映射文件涉及到`ORM`概念：

- `O`对象：`Java`实体类对象
- `R`关系：关系型数据库
- `M`映射：二者之间的关系[类-表，属性-字段/列，对象-记录/行]

映射文件的命名规则：

> 1. 表所对应的实体类类名+`Mapper.xml`
> 2. `MyBatis`可以面向接口操作数据，要保证两个一致：
>    - `mapper`接口的全类名和映射文件的命名空间`namespace`保持一致
>    - `mapper`接口中方法的方法名和映射文件中编写`SQL`的标签`id`属性保持一致

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kk.mapper.UserMapper">
    <!--int insertUser()-->
    <insert id="insertUser">
        insert into t_user values(1, "admin", "123456", 23, '男', "123@qq.com");
    </insert>
</mapper>
```

## `MyBatis`测试操作

`ctrl + p`可以查看方法可以存放的参数：

```java
package com.kk;

import com.kk.mapper.UserMapper;
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
            String result = userMapper.insertUser() == 1 ? "成功" : "失败";
            System.out.println("添加" + result);
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
```

## 添加`log4j`日志功能

`Maven`中导入`log4j`相关包：

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

配置`log4j`配置文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">

<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

    <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="UTF-8" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS} %m  (%F:%L) \n" />
        </layout>
    </appender>
    <logger name="java.sql">
        <level value="debug" />
    </logger>
    <logger name="org.apache.ibatis">
        <level value="info" />
    </logger>
    <root>
        <level value="debug" />
        <appender-ref ref="STDOUT" />
    </root>
</log4j:configuration>
```

日志级别：`FATAL[致命] > ERROR[错误] > WARN[警告] > DEBUG[调试]`

## `MyBatis`增删改查

注意查询语句需要有：`resultType[默认映射关系]/resultMap[自定义映射关系]`，否则不知道返回的是哪个`pojo`。

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kk.mapper.UserMapper">
    <!--int insertUser()-->
    <insert id="insertUser">
        insert into t_user
        values (1, "admin", "123456", 23, '男', "123@qq.com");
    </insert>

    <!--int deleteUser()-->
    <delete id="deleteUser">
        delete from t_user where id = 1;
    </delete>

    <!--int updateUser()-->
    <update id="updateUser">
        update t_user
        set username = "张三"
        where id = 1;
    </update>

    <!--int selectUser()-->
    <select id="selectUserById" resultType="com.kk.pojo.User">
        select *
        from t_user
        where id = 1;
    </select>
</mapper>
```

```java
package com.kk.mapper;

import com.kk.pojo.User;

public interface UserMapper {
    /**
     * 添加用户信息
     */
    public abstract int insertUser();

    /**
     * 删除用户信息
     */
    public abstract int deleteUser();

    /**
     * 修改用户信息
     */
    public abstract int updateUser();

    /**
     * 查询用户信息
     */
    public abstract User selectUserById();
}
```

```java
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
```

## `MyBatis`配置核心文件模板

**<font color="deepskyblue">`ctrl + shift + s` ---> `File and Code Templates` ---> 设置模板名称 + 后缀 ---> 导入模板即可</font>**

# 2022年06月23日上午

## `MyBatis`获取参数值的两种方式

获取参数的两种方式：`${}`和`#{}`

1. `${}`：本质是字符串拼接 ---> 可能会导致`SQL`注入 ---> 需要自行添加单引号
2. `#{}`：本质是占位符赋值 ---> 会使用`PreparedStatement`转化为`?`赋值

- **<font color="red">（1）如果`mapper`接口方法中的参数为单个的字面量类型</font>**

  1. 使用`${}`进行字符串拼接**需要使用单引**号 ---> `''`

  2. 不管使用的是`${}`字符串拼接还是`#{}`占位符赋值，字面量赋值的值名称可以随便取，比如这里取的是`aaa`

     ```sql
     select * from t_user where username = ?
     ```

     ```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE mapper
             PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
             "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     <mapper namespace="com.kk.mapper.UserMapper">
         <select id="selectUserByUsername" resultType="user">
             select * from t_user where username = #{aaa}
             <!--select * from t_user where username = '${aaa}'-->
         </select>
     </mapper>
     ```

     ```java
     package com.kk;
     
     import com.kk.mapper.UserMapper;
     import com.kk.pojo.User;
     import com.kk.util.SqlSessionUtil;
     import org.apache.ibatis.session.SqlSession;
     
     public class MyBatisTest {
         public static void main(String[] args) {
             SqlSession sqlSession = SqlSessionUtil.getSqlSession();
             UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
             //1.直接传递字面量 ---> mapper.xml 中不管用 ${} 还是 #{} 写任何东西都可以
             User user = userMapper.selectUserByUsername("admin");
             System.out.println(user);
         }
     }
     ```

- **<font color="red">（2）多个参数可以使用位置的方式给`SQL`语句赋值 ---> `arg0 arg1 param1 param2`</font>**

  - 以前学习的时候只学习：`#{arg0}、#{arg1}...`这种方式，而没有学习：`#{param0}、#{param1}...`这种方式还有：`'${arg0}'、'${arg1}'、'${param1}'、'${param2}'`

    ```xml
    <select id="loginCheck" resultType="user">
        <!--select * from t_user where username = #{arg0} and password = #{arg1}-->
        <!--select * from t_user where username = #{param1} and password = #{param2}-->
        <!--select * from t_user where username = '${arg0}' and password = '${arg1}'-->
        <!--select * from t_user where username = '${param1}' and password = '${param2}'-->
        <!--select * from t_user where username = #{arg0} and password = '${param2}'-->
        select * from t_user where username = '${param1}' and password = #{arg1}
    </select>
    ```

    ```java
    package com.kk;
    
    import com.kk.mapper.UserMapper;
    import com.kk.pojo.User;
    import com.kk.util.SqlSessionUtil;
    import org.apache.ibatis.session.SqlSession;
    
    public class MyBatisTest {
        public static void main(String[] args) {
            SqlSession sqlSession = SqlSessionUtil.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.loginCheck("admin", "123456");
            System.out.println(user);
        }
    }
    ```

- **<font color="red">（3）多个参数可以使用`Map`的方式给`SQL`语句赋值</font>**

  ```java
  public abstract User loginCheckByMap(Map map);
  ```

  ```xml
  <select id="loginCheckByMap" resultType="user">
      select * from t_user where username = #{username} and password = '${password}'
  </select>
  ```

  ```java
  package com.kk;
  
  import com.kk.mapper.UserMapper;
  import com.kk.pojo.User;
  import com.kk.util.SqlSessionUtil;
  import org.apache.ibatis.session.SqlSession;
  
  import java.util.HashMap;
  import java.util.Map;
  
  public class MyBatisTest {
      public static void main(String[] args) {
          SqlSession sqlSession = SqlSessionUtil.getSqlSession();
          UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
          Map map = new HashMap();
          map.put("username", "admin");
          map.put("password", "123456");
          User user = userMapper.loginCheckByMap(map);
          System.out.println(user);
      }
  }
  ```

- **<font color="red">（4）多个参数可以使用实体类的方式给`SQL`语句赋值：注意这里的实体类属性名其实是从`getter()`方法中获取的，也就是说如果`getter()`方法中的名字有变换，那在`SQL`语句中也应该体现出相应的变化</font>**

  ```java
  public abstract int insertUser(User user);
  ```

  ```java
  <insert id="insertUser">
      insert into t_user values(#{id}, #{username}, '${password}', #{age}, #{sex}, #{email})
  </insert>
  ```

  ```java
  package com.kk;
  
  import com.kk.mapper.UserMapper;
  import com.kk.pojo.User;
  import com.kk.util.SqlSessionUtil;
  import org.apache.ibatis.session.SqlSession;
  
  public class MyBatisTest {
      public static void main(String[] args) {
          SqlSession sqlSession = SqlSessionUtil.getSqlSession();
          UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
          User user = new User(2, "张三", "123456", 100, "男", "456@qq.com");
          String result = userMapper.insertUser(user) == 1 ? "成功" : "失败";
          System.out.println("添加" + result);
      }
  }
  ```

- **<font color="red">（5）使用`@Param`传递注解，其本质上是一个`Map`</font>**

  ```java
  public abstract User loginCheckByParam(@Param(value = "aaa") String username, @Param(value = "bbb") String password);
  ```

  ```xml
  <select id="loginCheckByParam" resultType="user">
      <!--select * from t_user where username = #{aaa} and password = #{bbb}-->
      select * from t_user where username = #{param1} and password = #{param2}
  </select>
  ```

  ```java
  package com.kk;
  
  import com.kk.mapper.UserMapper;
  import com.kk.pojo.User;
  import com.kk.util.SqlSessionUtil;
  import org.apache.ibatis.session.SqlSession;
  
  public class MyBatisTest {
      public static void main(String[] args) {
          SqlSession sqlSession = SqlSessionUtil.getSqlSession();
          UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
          User user = userMapper.loginCheckByParam("admin", "123456");
          System.out.println(user);
      }
  }
  ```

  **<font color="deepskyblue">观察`@Param`的源码</font>**：

  `ParamNameResolver`类中的`getNamedParams()`方法是关键：

  - 该方法从接口中获取传递进来的`param`的`value`值和对应的具体值，从源码也可以看到最后面返回的就是：`param`，其本质就是个`Map`，存储着键值对。

  - 注意这里的：`!this.names.containsValue(genericParamName)`

    `genericParamName`就是上面定义的`param1`跟`param2`等，表示如果`@Param(value = "param1")`也就是`param1`就是`@Param`的值，则不存放到`param`这个`Map`集合中，如果不是则存放。

    比如我这里存放的是：`public abstract User loginCheckByParam(@Param(value = "username") String username, @Param(value = "password") String password);`

    则最后整个`param Map`存储的是：

    > `username ---> admin`
    >
    > `param1 ---> admin`
    >
    > `password ---> 123456`
    >
    > `param2 ---> 123456`

  ```java
  public Object getNamedParams(Object[] args) {
      int paramCount = this.names.size();
      if (args != null && paramCount != 0) {
          if (!this.hasParamAnnotation && paramCount == 1) {
              return args[(Integer)this.names.firstKey()];
          } else {
              Map<String, Object> param = new ParamMap();
              int i = 0;
              for(Iterator var5 = this.names.entrySet().iterator(); var5.hasNext(); ++i) {
                  Entry<Integer, String> entry = (Entry)var5.next();
                  param.put((String)entry.getValue(), args[(Integer)entry.getKey()]);
                  String genericParamName = "param" + String.valueOf(i + 1);
                  if (!this.names.containsValue(genericParamName)) {
                      param.put(genericParamName, args[(Integer)entry.getKey()]);
                  }
              }
              return param;
          }
      } else {
          return null;
      }
  }
  ```

  整个`MyBatis`最终执行`SQL`语句的方法在`MapperMethod`类中的`execute()`方法中：

  ```java
  public Object execute(SqlSession sqlSession, Object[] args) {
          Object result;
          Object param;
          switch(this.command.getType()) {
          case INSERT:
              param = this.method.convertArgsToSqlCommandParam(args);
              result = this.rowCountResult(sqlSession.insert(this.command.getName(), param));
              break;
          case UPDATE:
              param = this.method.convertArgsToSqlCommandParam(args);
              result = this.rowCountResult(sqlSession.update(this.command.getName(), param));
              break;
          case DELETE:
              param = this.method.convertArgsToSqlCommandParam(args);
              result = this.rowCountResult(sqlSession.delete(this.command.getName(), param));
              break;
          case SELECT:
              if (this.method.returnsVoid() && this.method.hasResultHandler()) {
                  this.executeWithResultHandler(sqlSession, args);
                  result = null;
              } else if (this.method.returnsMany()) {
                  result = this.executeForMany(sqlSession, args);
              } else if (this.method.returnsMap()) {
                  result = this.executeForMap(sqlSession, args);
              } else if (this.method.returnsCursor()) {
                  result = this.executeForCursor(sqlSession, args);
              } else {
                  param = this.method.convertArgsToSqlCommandParam(args);
                  result = sqlSession.selectOne(this.command.getName(), param);
                  if (this.method.returnsOptional() && (result == null || !this.method.getReturnType().equals(result.getClass()))) {
                      result = Optional.ofNullable(result);
                  }
              }
              break;
          case FLUSH:
              result = sqlSession.flushStatements();
              break;
          default:
              throw new BindingException("Unknown execution method for: " + this.command.getName());
          }
  
          if (result == null && this.method.getReturnType().isPrimitive() && !this.method.returnsVoid()) {
              throw new BindingException("Mapper method '" + this.command.getName() + " attempted to return null from a method with a primitive return type (" + this.method.getReturnType() + ").");
          } else {
              return result;
          }
      }
  }
  ```

# 2022年06月23日下午

## `MyBatis`各种查询功能

对应的`resultType`对应关系：

- 基本数据类型对应的都是 ---> `_基本数据类型`
- `int`基本数据类型比较特殊，它有两个：`_int`和`_integer`
- 包装类跟`String`类在数据库中都对应着是：小写的包装类跟小写的`strin`

**<font color="red">如果要把多条数据全部放进一个`Map`而不是放进`List`，则需要添加如：`@Mapkey(value = "id")`即将`id`当作是一个`key`，其余的作为`value`</font>**

```java
@MapKey(value = "id")
public abstract Map<String, Object> selectUsersToMap();
```

```xml
<select id="selectUsersToMap" resultType="map">
    select * from t_user
</select>
```

```java
package com.kk;

import com.kk.mapper.UserMapper;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = userMapper.selectUsersToMap();
        System.out.println(map);
    }
}
```

## `MyBatis`模糊查询

- **第一种方式：**模糊查询需要使用到单引号，如果是占位符赋值的方式`#{}`则转换成`?`之后，会被当作字符串的一部分，所以可以直接使用`${}`
- **第二种方式：**使用字符串拼接函数即`concat()`
- **第三种方式：**`"%"#{username}"%"`

```java
public abstract User selectUserByI(String username);
```

```java
package com.kk;

import com.kk.mapper.UserMapper;
import com.kk.pojo.User;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectUserByI("a");
        System.out.println(user);
    }
}
```

```xml
<select id="selectUserByI" resultType="user">
    <!--select * from t_user where username like '%${username}%'-->
    <!--select * from t_user where username like concat('%', #{username}, '%')-->
    select * from t_user where username like "%"#{username}"%"
</select>
```

## `MyBatis`批量删除

批量删除需要使用`in`：`select * from t_user where id in(1, 2, 3)`注意这里是不能有单引号的。

因为使用`#{}`是会自动加上单引号的而且是在最外层加上单引号不管里面的，又因为`in`里面不能有单引号，所以你是不可以使用`#{}`的只能使用`${}`。

```java
public abstract int deleteMore(String usernames);
```

```xml
<delete id="deleteMore">
    delete from t_user where username in (${usernames})
</delete>
```

```java
package com.kk;

import com.kk.mapper.UserMapper;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int result = userMapper.deleteMore("'admin', 'bdmin'");
        System.out.println(result);
    }
}
```

## `MyBatis`动态设置表名

表名是不能加单引号的，这跟上面的批量删除原理是一样的，所以设置表名也同样跟批量删除一样需要使用`${}`字符串拼接的方式。

## `MyBatis`获取自动递增的主键

在`mapper.xml`中使用，需要使用到：

- `useGeneratedKeys`：是否对主键使用自动递增
- `keyProperty`：将自增的主键的值赋值给传输到映射文件中参数的某个属性

**特别注意：需要先到`Navicat`中勾选上主键的自动递增选项**

`UserMapper.xml`：

```xml
<insert id="insertUserGeneratedKeys" useGeneratedKeys="true" keyProperty="id">
    insert into t_user(username, password, age, sex, email) values(#{username}, #{password}, #{age}, #{sex}, #{email})
</insert>
```

`UserMapper.java`

```java
public abstract int insertUserGeneratedKeys(User user);
```

`MyBatisTest.java`

```java
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
```

## `MyBatis`之`SQL`语句起别名绕过实体类名跟字段名不同的场景

- 适用于实体类的名称跟数据库字段名不同的场景下

比如：现有一张`t_emp`表，表中员工姓名的字段名为：`emp_name`，但是在实体类中名为：`empName`，直接查找出来的结果为：可以看到`empName`的结果为`null`

```java
Emp{eid=1, empName='null', age=18, sex='男', email='zhangsan@qq.com', dept=null}
Emp{eid=2, empName='null', age=19, sex='男', email='lisi@qq.com', dept=null}
Emp{eid=3, empName='null', age=20, sex='男', email='wangwu@qq.com', dept=null}
Emp{eid=4, empName='null', age=21, sex='男', email='zhaoliu@qq.com', dept=null}
Emp{eid=5, empName='null', age=22, sex='女', email='zhuqi@qq.com', dept=null}
```

因为`empName != emp_name`所以得到的结果为`null`，这很好理解，我们可以使用：赋予别名的方式[本质是通过修改`SQL`语句]

```sql
select eid, emp_name empName, age, sex, email from t_emp
```

## `MyBatis`之`mapUnderscoreToCamelCase`自动起驼峰名绕过实体类名跟字段名不同的场景

`mybatis-config.xml`：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="jdbc.properties"></properties>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <typeAliases>
        <package name="com.kk.pojo"/>
    </typeAliases>
    <environments default="mysql">
        <environment id="mysql">
            <transactionManager type="JDBC"></transactionManager>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <package name="com.kk.mapper"/>
    </mappers>
</configuration>
```

`EmpMapper.xml`：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kk.mapper.EmpMapper">
    <select id="selectAllEmp" resultType="emp">
        select * from t_emp
        <!--select eid, emp_name empName, age, sex, email from t_emp-->
    </select>
</mapper>
```

## `MyBatis`之`ResultMap`绕过实体类名跟字段名不同的场景

前提条件：字段必须符合字段的规则，属性名必须符合属性的规则

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kk.mapper.EmpMapper">
    
    <resultMap id="empResultMap" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="email" column="email"></result>
    </resultMap>
    
    <select id="selectAllEmp" resultMap="empResultMap">
        select * from t_emp
        <!--select eid, emp_name empName, age, sex, email from t_emp-->
    </select>
</mapper>
```

## `MyBatis`解决多对一映射关系

**例子：`Emp`实体类含有`Dept dept;`属性**

通过联级属性可以解决多对一的映射关系。

第一种解决方式：通过传统逻辑

> ```xml
> <?xml version="1.0" encoding="UTF-8" ?>
> <!DOCTYPE mapper
>         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
>         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
> <mapper namespace="com.kk.mapper.EmpMapper">
>     <resultMap id="empAndDeptResultMap" type="emp">
>         <id property="eid" column="eid"></id>
>         <result property="empName" column="emp_name"></result>
>         <result property="age" column="age"></result>
>         <result property="email" column="email"></result>
>         <result property="dept.did" column="did"></result>
>         <result property="dept.deptName" column="dept_name"></result>
>     </resultMap>
>     
>     <select id="selectAllEmp" resultMap="empAndDeptResultMap">
>         SELECT * FROM t_emp left join t_dept on t_emp.did = t_dept.did
>     </select>
> </mapper>
> ```

第二种解决方式：通过`association`

> ```xml
> <?xml version="1.0" encoding="UTF-8" ?>
> <!DOCTYPE mapper
>         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
>         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
> <mapper namespace="com.kk.mapper.EmpMapper">
>     <resultMap id="empAndDeptAssociationResultMap" type="emp">
>         <id property="eid" column="eid"></id>
>         <result property="empName" column="emp_name"></result>
>         <result property="age" column="age"></result>
>         <result property="email" column="email"></result>
>         <association property="dept" javaType="dept">
>             <id property="did" column="did"></id>
>             <result property="deptName" column="dept_name"></result>
>         </association>
>     </resultMap>
>     
>     <select id="selectAllEmp" resultMap="empAndDeptAssociationResultMap">
>         SELECT * FROM t_emp left join t_dept on t_emp.did = t_dept.did
>     </select>
> </mapper>
> ```

第三种解决方式：`association` + 分步查询**<font color="red">【用的最多 + 很重要】</font>**

> 1. 查询员工信息`EmpMapper.java + EmpMapper.xml`**【最重要最主要的就是这里的`association`部分】**
>
>    ```java
>    package com.kk.mapper;
>    
>    import com.kk.pojo.Emp;
>    
>    import java.util.List;
>    
>    public interface EmpMapper {
>        public abstract List<Emp> selectStepOne();
>    }
>    ```
>
>    ```xml
>    <?xml version="1.0" encoding="UTF-8" ?>
>    <!DOCTYPE mapper
>            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
>            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
>    <mapper namespace="com.kk.mapper.EmpMapper">
>        <resultMap id="selectStepOneInEmp" type="emp">
>            <id property="eid" column="eid"></id>
>            <result property="empName" column="emp_name"></result>
>            <result property="age" column="age"></result>
>            <result property="email" column="email"></result>
>            <association property="dept" select="com.kk.mapper.DeptMapper.selectStepTwoByDId" column="did"></association>
>        </resultMap>
>    
>        <select id="selectStepOne" resultMap="selectStepOneInEmp">
>            select * from t_emp where eid
>        </select>
>    </mapper>
>    ```
>
> 2. 根据查询到的员工返回的`id`查询部门信息：`DeptMapper.java + DeptMapper.xml`
>
>    ```java
>    package com.kk.mapper;
>             
>    import com.kk.pojo.Dept;
>    import org.apache.ibatis.annotations.Param;
>             
>    public interface DeptMapper {
>        public abstract Dept selectStepTwoByDId(@Param(value = "did") Integer did);
>    }
>    ```
>
>    ```xml
>    <?xml version="1.0" encoding="UTF-8" ?>
>    <!DOCTYPE mapper
>            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
>            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
>    <mapper namespace="com.kk.mapper.DeptMapper">
>                 
>        <resultMap id="deptResultMap" type="dept">
>            <id property="did" column="did"></id>
>            <result property="deptName" column="dept_name"></result>
>        </resultMap>
>                 
>        <select id="selectStepTwoByDId" resultMap="deptResultMap">
>            select * from t_dept where did = #{did}
>        </select>
>    </mapper>
>    ```

## `MyBatis`分布查询的好处

- **<font color="red">既然可以一步就使用`SQL`语句查询出结果，为什么还要使用分步查询呢？</font>**

  - 这是因为分布查询可以实现延迟加载即懒加载，也就是按需加载，只有真正要用的时候才去加载。

  - 若要使用延迟加载，首先你得是一个分步查询，然后你得在`MyBatis`核心配置文件中配置一下，需要配置的如下相关的有：

    `lazyLoadingEnabled`：延迟加载的开关。当开启时，所有的关联对象都会延迟加载

    `aggressiveLazyLoading`：开启时将立即加载，所以需要关闭【默认就是关闭`false`的】

    故所需要延迟加载则需要在`mybatis-config.xml`这样配置：

    ```xml
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="lazyLoadingEnabled" value="true"/>
    </settings>
    ```

    当没有开启时，当你在`MyBatisTest.java`中要获取`emp.getName()`是这样的：可以看到你只想要的是`empName`，但是它还是执行了第二步的`SQL`语句

    ```java
    DEBUG 06-23 17:17:03,992 ==>  Preparing: select * from t_emp where eid   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,018 ==> Parameters:   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,035 ====>  Preparing: select * from t_dept where did = ?   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,036 ====> Parameters: 1(Integer)  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,038 <====      Total: 1  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,041 ====>  Preparing: select * from t_dept where did = ?   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,042 ====> Parameters: 2(Integer)  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,043 <====      Total: 1  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,044 ====>  Preparing: select * from t_dept where did = ?   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,044 ====> Parameters: 3(Integer)  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,045 <====      Total: 1  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:17:04,047 <==      Total: 5  (BaseJdbcLogger.java:143) 
    张三
    李四
    王五
    赵六
    田七
    ```

    当你打开了延迟加载，你将看到这样的结果：可以看到它只执行了第一步的`SQL`语句，这就大大提高了效率！！！

    ```java
    DEBUG 06-23 17:18:23,973 ==>  Preparing: select * from t_emp where eid   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:18:23,998 ==> Parameters:   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:18:24,049 <==      Total: 5  (BaseJdbcLogger.java:143) 
    张三
    李四
    王五
    赵六
    田七
    ```

    这也还是有个问题，就是你所有的分步查询都实现了延迟加载了，有时候我就想这个分布查询不做延迟加载可以吗？当然可以！你需要在`xxxMapper.xml`文件中的`resultMap`中设置下：`fetchType`属性，它有两个值：`lazy/eager`

    只要你在`MyBatis`核心配置文件中打开了`lazyLoadingEnabled`那么这个`fetchType`默认就是`lazy`的，此时你不想要延迟加载，你可以设置：`fetchType=eager`

    ```xml
    <resultMap id="selectStepOneInEmp" type="emp">
        <id property="eid" column="eid"></id>
        <result property="empName" column="emp_name"></result>
        <result property="age" column="age"></result>
        <result property="email" column="email"></result>
        <association property="dept" select="com.kk.mapper.DeptMapper.selectStepTwoByDId" column="did" fetchType="eager"></association>
    </resultMap>
    <select id="selectStepOne" resultMap="selectStepOneInEmp">
        select * from t_emp where eid
    </select>
    ```

    然后就可以看到这个`resultMap`中没有延迟加载了：

    ```java
    DEBUG 06-23 17:23:15,040 ==>  Preparing: select * from t_emp where eid   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,065 ==> Parameters:   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,082 ====>  Preparing: select * from t_dept where did = ?   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,083 ====> Parameters: 1(Integer)  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,085 <====      Total: 1  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,088 ====>  Preparing: select * from t_dept where did = ?   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,088 ====> Parameters: 2(Integer)  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,089 <====      Total: 1  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,090 ====>  Preparing: select * from t_dept where did = ?   (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,090 ====> Parameters: 3(Integer)  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,091 <====      Total: 1  (BaseJdbcLogger.java:143) 
    DEBUG 06-23 17:23:15,092 <==      Total: 5  (BaseJdbcLogger.java:143) 
    张三
    李四
    王五
    赵六
    田七
    ```

## `MyBatis`解决一对多映射关系

**例子：`Dept`实体类含有`List<Emp> emps;`属性**

多对一有上面所讲的：`association`和`.`的方式，但是一对多是没有的，一对多要使用的是`collection`获取的是集合类型

第一种解决方式：使用`collection[ofType]`

> ```xml
> <?xml version="1.0" encoding="UTF-8" ?>
> <!DOCTYPE mapper
>         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
>         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
> <mapper namespace="com.kk.mapper.DeptMapper">
>     
>     <resultMap id="deptCollectionResultMap" type="dept">
>         <id property="did" column="did"/>
>         <result property="deptName" column="dept_name"/>
>         <collection property="emps" ofType="emp">
>             <id property="eid" column="eid"/>
>             <result property="empName" column="emp_name"/>
>             <result property="age" column="age"/>
>             <result property="sex" column="sex"/>
>             <result property="email" column="email"/>
>         </collection>
>     </resultMap>
> 
>     <select id="selectCollection" resultMap="deptCollectionResultMap">
>         select * from t_dept left join t_emp on t_dept.did = t_emp.did
>     </select>
> </mapper>
> ```
>
> ```java
> public abstract List<Dept> selectCollection();
> ```
>
> ```java
> package com.kk;
> 
> import com.kk.mapper.DeptMapper;
> import com.kk.pojo.Dept;
> import com.kk.util.SqlSessionUtil;
> import org.apache.ibatis.session.SqlSession;
> 
> import java.util.List;
> 
> public class MyBatisTest {
>     public static void main(String[] args) {
>         SqlSession sqlSession = SqlSessionUtil.getSqlSession();
>         DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
>         List<Dept> deptList = deptMapper.selectCollection();
>         for (Dept dept : deptList) System.out.println(dept);
>     }
> }
> ```
>
> 结果如下：
>
> ```java
> DEBUG 06-23 17:36:15,583 ==>  Preparing: select * from t_dept left join t_emp on t_dept.did = t_emp.did   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:36:15,612 ==> Parameters:   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:36:15,630 <==      Total: 5  (BaseJdbcLogger.java:143) 
> Dept{did=1, deptName='国务院', emps=[Emp{eid=4, empName='赵六', age=21, sex='男', email='zhaoliu@qq.com', dept=null}, Emp{eid=1, empName='张三', age=18, sex='男', email='zhangsan@qq.com', dept=null}]}
> Dept{did=2, deptName='主席办公室', emps=[Emp{eid=5, empName='田七', age=22, sex='女', email='zhuqi@qq.com', dept=null}, Emp{eid=2, empName='李四', age=19, sex='男', email='lisi@qq.com', dept=null}]}
> Dept{did=3, deptName='总理办公室', emps=[Emp{eid=3, empName='王五', age=20, sex='男', email='wangwu@qq.com', dept=null}]}
> ```

第二种解决方式：**分布查询**

> 1. 查询部门信息：`DeptMapper.java + DeptMapper.xml`
>
>    ```java
>    public abstract List<Dept> selectCollectionStepOne();
>    ```
>
>    ```xml
>    <resultMap id="deptCollectionOne" type="dept">
>        <id property="did" column="did"/>
>        <result property="deptName" column="dept_name"/>
>        <collection property="emps" select="com.kk.mapper.EmpMapper.selectCollectionStepTwo" column="did" fetchType="lazy"/>
>    </resultMap>
>    <select id="selectCollectionStepOne" resultMap="deptCollectionOne">
>        select * from t_dept
>    </select>
>    ```
>
> 2. 通过部门信息中的`did`获取员工集合
>
>    ```java
>    public abstract List<Emp> selectCollectionStepTwo(Integer did);
>    ```
>
>    ```xml
>    <resultMap id="selectCollectionStepTwoMap" type="emp">
>        <id property="eid" column="eid"></id>
>        <result property="empName" column="emp_name"></result>
>        <result property="age" column="age"></result>
>        <result property="sex" column="sex"></result>
>        <result property="email" column="email"></result>
>    </resultMap>
>    <select id="selectCollectionStepTwo" resultMap="selectCollectionStepTwoMap">
>        select * from t_emp where did = #{did}
>    </select>
>    ```
>
> 获取到的结果如下：
>
> ```java
> DEBUG 06-23 17:50:05,052 ==>  Preparing: select * from t_dept   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,078 ==> Parameters:   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,126 <==      Total: 3  (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,128 ==>  Preparing: select * from t_emp where did = ?   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,129 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,130 <==      Total: 2  (BaseJdbcLogger.java:143) 
> Dept{did=1, deptName='国务院', emps=[Emp{eid=1, empName='张三', age=18, sex='男', email='zhangsan@qq.com', dept=null}, Emp{eid=4, empName='赵六', age=21, sex='男', email='zhaoliu@qq.com', dept=null}]}
> DEBUG 06-23 17:50:05,130 ==>  Preparing: select * from t_emp where did = ?   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,130 ==> Parameters: 2(Integer)  (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,131 <==      Total: 2  (BaseJdbcLogger.java:143) 
> Dept{did=2, deptName='主席办公室', emps=[Emp{eid=2, empName='李四', age=19, sex='男', email='lisi@qq.com', dept=null}, Emp{eid=5, empName='田七', age=22, sex='女', email='zhuqi@qq.com', dept=null}]}
> DEBUG 06-23 17:50:05,131 ==>  Preparing: select * from t_emp where did = ?   (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,132 ==> Parameters: 3(Integer)  (BaseJdbcLogger.java:143) 
> DEBUG 06-23 17:50:05,133 <==      Total: 1  (BaseJdbcLogger.java:143) 
> Dept{did=3, deptName='总理办公室', emps=[Emp{eid=3, empName='王五', age=20, sex='男', email='wangwu@qq.com', dept=null}]}
> ```

一样的，这里是分布查询，所以可以做延迟加载提高性能。

# 2022年06月23日晚上

## `MyBatis`动态`SQL`

动态`SQL`是一种根据特定条件动态拼接`SQL`语句的功能，它存在的意义是为了解决拼接`SQL`语句拼接字符串时的痛点问题。本质就是对`SQL`语句进行拼接数据。

1. **`if`标签**【为了与`and`联用，加上` 1 = 1`恒成立即可】

   ```java
   public abstract Emp selectEmpByCondition(@Param(value = "empName") String empName, @Param(value = "age") Integer age, @Param(value = "sex") String sex, @Param(value = "email") String email);
   ```

   ```xml
   <select id="selectEmpByCondition" resultType="emp">
       select * from t_emp where 1 = 1
       <if test="empName != null and empName != ''">
           emp_name = #{empName}
       </if>
       <if test="age != null and age != ''">
           and age = #{age}
       </if>
       <if test="sex != null and sex != ''">
           and sex = #{sex}
       </if>
       <if test="email != null and email != ''">
           and email = #{email}
       </if>
   </select>
   ```

   ```java
   package com.kk;
   
   import com.kk.mapper.EmpMapper;
   import com.kk.pojo.Emp;
   import com.kk.util.SqlSessionUtil;
   import org.apache.ibatis.session.SqlSession;
   
   public class MyBatisTest {
       public static void main(String[] args) {
           SqlSession sqlSession = SqlSessionUtil.getSqlSession();
           EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
           Emp emp = empMapper.selectEmpByCondition("张三", 18, "男", "zhangsan@qq.com");
           System.out.println(emp);
       }
   }
   ```

2. **`where`标签**【自动添加`where`关键字，并且会动态删除`and/or`，此外特别注意`where`标签是无法去除语句后面的`and`的，只能去掉语句前面的`and`】

   ```xml
   <select id="selectEmpByCondition" resultType="emp">
       select * from t_emp
       <where>
           <if test="empName != null and empName != ''">
               emp_name = #{empName} and
           </if>
           <if test="age != null and age != ''">
               and age = #{age}
           </if>
           <if test="sex != null and sex != ''">
               and sex = #{sex}
           </if>
           <if test="email != null and email != ''">
               and email = #{email}
           </if>
       </where>
   </select>
   ```

3. **`trim`标签**【`prefix suffix`：将`trim`标签中内容前面或后面添加指定内容，`prefixOverride suffixOverride`：将`trim`标签中内容前面或者后面添加指定内容】

   ```xml
   <select id="selectEmpByCondition" resultType="emp">
       select * from t_emp
       <trim prefix="where" suffixOverrides="and|or">
           <if test="empName != null and empName != ''">
               emp_name = #{empName} and
           </if>
           <if test="age != null and age != ''">
               age = #{age} or
           </if>
           <if test="sex != null and sex != ''">
               sex = #{sex} and
           </if>
           <if test="email != null and email != ''">
               email = #{email}
           </if>
       </trim>
   </select>
   ```

4. **`choose when otherwise`标签**【其中的`when otherwise`相当于`if...else if...else`】

   ```xml
   <select id="selectEmpByCondition" resultType="emp">
       select * from t_emp
       <where>
           <choose>
               <when test="empName != null and empName != ''">
                   emp_name = #{empName}
               </when>
               <when test="age != null and age != ''">
                   age = #{age}
               </when>
               <when test="sex != null and sex != ''">
                   sex = #{sex}
               </when>
               <when test="email != null and email != ''">
                   email = #{email}
               </when>
               <otherwise>
                   did = 1
               </otherwise>
           </choose>
       </where>
   </select>
   ```

5. **`foreach`标签**【循环标签`collection item separator open close`】

   批量删除：

   ```java
   public abstract int deleteEmpByForeach(@Param(value = "eIds") Integer[] eIds);
   ```

   ```xml
   <delete id="deleteEmpByForeach">
       delete from t_emp where eid in
       <foreach collection="eIds" item="eid" separator="," open="(" close=")">
           #{eid}
       </foreach>
   </delete>
   
   <delete id="deleteEmpByForeach">
       delete from t_emp where
       <foreach collection="eIds" item="eid" separator="or">
           eid = #{eid}
       </foreach>
   </delete>
   ```

6. **`sql`标签**【与`<include refied="">`】

   在添加操作的时候时常用到：

   ```xml
   <sql id="empColumns">emp_name, age, sex, email</sql>
   
   <insert id="insertEmpBySqlIncludeRefied">
       insert into t_emp(<include refied="empColumns">) values(...)
   </delete>
   ```

# 2022年06月24日上午

## `MyBatis`一级缓存

`MyBatis`的缓存指的是它会将查询出来的数据进行一个记录，等下次查询相同数据的时候就会从缓存取不会从数据库中去存储。可想而知，缓存缓存的是查询的记录，而不是增删改。

`MyBatis`的一级缓存是默认开启的，比如我们连续查询同一个东西，则日志记录到只会执行一次`Sql`语句。**<font color="red">只要你是同一个`SqlSession`并且是同一个查询即`SQL`语句相同那就会走缓存。</font>**

可以看到下列代码输出结果只记录了一条`Sql`语句，但是确实输出了两条结果，这就用到了一级缓存。

```java
package com.kk;

import com.kk.mapper.CacheMapper;
import com.kk.pojo.Emp;
import com.kk.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class MyBatisTest {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        CacheMapper cacheMapper = sqlSession.getMapper(CacheMapper.class);
        Emp emp1 = cacheMapper.selectEmpById(1);
        System.out.println(emp1);
        Emp emp2 = cacheMapper.selectEmpById(1);
        System.out.println(emp2);
    }
}

DEBUG 06-24 11:09:07,105 ==>  Preparing: select * from t_emp where eid = ?   (BaseJdbcLogger.java:143) 
DEBUG 06-24 11:09:07,129 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:143) 
DEBUG 06-24 11:09:07,148 <==      Total: 1  (BaseJdbcLogger.java:143) 
Emp{eid=1, empName='张三', age=18, sex='男', email='zhangsan@qq.com', dept=null}
Emp{eid=1, empName='张三', age=18, sex='男', email='zhangsan@qq.com', dept=null}
```

使一级缓存失效的情况：

```shell
1. 不同的 SqlSession 对应着不同的一级缓存
2. 相同的 SqlSession 但是查询条件不同
3. 相同的 SqlSession 但是两次查询操作中间有任何一次增删改操作
4. 相同的 SqlSession 和相同的查询条件但是手动清除了缓存
```

## `MyBatis`二级缓存

二级缓存是`SqlSessionFactory`级别的，也就是比一级缓存的级别要大一些，只要是通过同一个`SqlSessionFactory`创建的`SqlSession`查询的结果都会被缓存。此后若再执行相同的查询语句，结果就会从缓存中获取。

二级缓存默认不是开启的，一级缓存是默认开启的，开启二级缓存需要满足以下条件：

```java
1. 在核心配置文件即`mybatis-config`中，设置全局配置<settings>中的属性cacheEnabled="true"，默认为true，不需要设置
2. 在映射文件中设置标签<cache/>
3. 二级缓存必须在 SqlSession 关闭或提交之后有效，否则数据将保存在一级缓存中，只有在关闭/提交了之后才会保存到二级缓存中
4. 查询的数据所转换的实体类类型必须实现序列化的接口即 implements Serializable
```

二级缓存的失效情况：

```java
1. 相同的 SqlSessionFactory 但是两次查询操作中间有任何一次增删改操作
```

## `MyBatis`缓存查询的顺序

- 先查询二级缓存，因为二级缓存中可能有其它程序已经查出来的数据，可以直接拿来使用
- 如果二级缓存没有命中，再查询一级缓存
- 如果一级缓存也没有名字，则查询数据库
- `SqlSession`关闭后，一级缓存中的数据会写入到二级缓存中

## `MyBatis`整合第三方缓存`EHCache`

`pom.xml`导包：

```xml
<!--MyBatis EHCache 整合包-->
<dependency>
    <groupId>org.mybatis.caches</groupId>
    <artifactId>mybatis-ehcache</artifactId>
    <version>1.2.2</version>
</dependency>
<!--slf4j日志门面的一个具体实现-->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

创建`ehcache.xml`配置文件：

```xml
<?xml version="1.0" encoding="utf-8" ?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
	<!-- 磁盘保存路径 -->
	<diskStore path="D:\atguigu\ehcache"/>
	<defaultCache
		maxElementsInMemory="1000"
		maxElementsOnDisk="10000000"
		eternal="false"
		overflowToDisk="true"
		timeToIdleSeconds="120"
		timeToLiveSeconds="120"
		diskExpiryThreadIntervalSeconds="120"
		memoryStoreEvictionPolicy="LRU">
	</defaultCache>
</ehcache>
```

配置日志文件：`SLF4J`，之前的简单日志`log4j`将失效。创建`logback.xml`作为`slf4j`的核心配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger]
                [%msg]%n
            </pattern>
        </encoder>
    </appender>
    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="DEBUG">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT"/>
    </root>
    <!-- 根据特殊需求指定局部日志级别 -->
    <logger name="com.kk.mapper" level="DEBUG"/>
</configuration>
```

在`MyBatis`的`xxxMapper.xml`映射文件中设置二级缓存的类型

- `<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>`

## `MyBatis`逆向工程

**通常使用奢华尊享版。**

`MyBatis`逆向工程本质就是一个代码生成器。有逆向工程就有正向工程。

- 正向工程：先创建实体类，然后由框架负责根据实体类生成数据库表。
- 逆向工程：先创建数据库表，由框架负责根据数据库表，反向生成：实体类、`Mapper`接口、`Mapper`映射文件【听起来就很爽的样子】

1. 添加依赖和插件

   ```xml
   <build>
       <plugins>
           <plugin>
               <groupId>org.mybatis.generator</groupId>
               <artifactId>mybatis-generator-maven-plugin</artifactId>
               <version>1.3.0</version>
               <dependencies>
                   <dependency>
                       <groupId>org.mybatis.generator</groupId>
                       <artifactId>mybatis-generator-core</artifactId>
                       <version>1.3.2</version>
                   </dependency>
                   <dependency>
                       <groupId>com.mchange</groupId>
                       <artifactId>c3p0</artifactId>
                       <version>0.9.2</version>
                   </dependency>
                   <dependency>
                       <groupId>mysql</groupId>
                       <artifactId>mysql-connector-java</artifactId>
                       <version>8.0.28</version>
                   </dependency>
               </dependencies>
           </plugin>
       </plugins>
   </build>
   ```

2. 创建`MyBatis`核心配置文件：`mybatis-config.xml`

3. 创建`MyBatis`逆向工程的配置文件：`generatorConfig.xml`

   > 文件名必须是：`generatorConfig.xml`【爆红的地方不用理会】

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE generatorConfiguration
           PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
           "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
   <generatorConfiguration>
       <!--
       targetRuntime: 执行生成的逆向工程的版本
       MyBatis3Simple: 生成基本的CRUD（清新简洁版）
       MyBatis3: 生成带条件的CRUD（奢华尊享版）
       -->
       <context id="DB2Tables" targetRuntime="MyBatis3Simple">
           <!-- 数据库的连接信息 -->
           <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                           connectionURL="jdbc:mysql://localhost:3306/mybatis_study?useSSL=false&amp;serverTimezone=Asia/Shanghai"
                           userId="root"
                           password="123456">
           </jdbcConnection>
           <!-- javaBean的生成策略-->
           <javaModelGenerator targetPackage="com.kk.pojo"
                               targetProject=".\src\main\java">
               <property name="enableSubPackages" value="true" />
               <property name="trimStrings" value="true" />
           </javaModelGenerator>
           <!-- SQL映射文件的生成策略 -->
           <sqlMapGenerator targetPackage="com.kk.mapper"
                            targetProject=".\src\main\resources">
               <property name="enableSubPackages" value="true" />
           </sqlMapGenerator>
           <!-- Mapper接口的生成策略 -->
           <javaClientGenerator type="XMLMAPPER"
                                targetPackage="com.kk.mapper" targetProject=".\src\main\java">
               <property name="enableSubPackages" value="true" />
           </javaClientGenerator>
           <!-- 逆向分析的表 -->
           <!-- tableName设置为*号，可以对应所有表，此时不写domainObjectName -->
           <!-- domainObjectName属性指定生成出来的实体类的类名 -->
           <table tableName="t_emp" domainObjectName="Emp"/>
           <table tableName="t_dept" domainObjectName="Dept"/>
       </context>
   </generatorConfiguration>
   ```

4. 双击插件中的：`mybatis-generator`即可生成实体类+`Mapper`映射文件+`Mapper`相关接口

   生成版本有清新简洁版和奢华尊享版两个版本，前者只会生成五个功能：增删改查【包括查全部和查一个】，而奢华尊享版有的功能基本都有。

   基本我们平常使用的都是尊享奢华版！

   ```java
   package com.kk;
   
   import com.kk.mapper.EmpMapper;
   import com.kk.pojo.Emp;
   import com.kk.util.SqlSessionUtil;
   import org.apache.ibatis.session.SqlSession;
   
   import java.util.List;
   
   public class MyBatisTest {
       public static void main(String[] args) {
           SqlSession sqlSession = SqlSessionUtil.getSqlSession();
           EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
           List<Emp> empList = empMapper.selectByExample(null);
           empList.forEach(System.out::println);
       }
   }
   ```

   ```java
   package com.kk;
   
   import com.kk.mapper.EmpMapper;
   import com.kk.pojo.Emp;
   import com.kk.pojo.EmpExample;
   import com.kk.util.SqlSessionUtil;
   import org.apache.ibatis.session.SqlSession;
   
   import java.util.List;
   
   public class MyBatisTest {
       public static void main(String[] args) {
           SqlSession sqlSession = SqlSessionUtil.getSqlSession();
           EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
           EmpExample empExample = new EmpExample();
           empExample.createCriteria().andEmpNameEqualTo("张三").andAgeGreaterThanOrEqualTo(10);
           empExample.or().andDidIsNotNull();
           List<Emp> empList = empMapper.selectByExample(empExample);
           System.out.println(empList);
       }
   }
   ```

增删改都有相对应的`selective`也就是可选的，就拿修改来说，如果你使用的是普通修改而不是选择性修改，则修改了`null`之后，那么字段会被修改成`null`，如果你选择性修改，那么字段就不会被修改成`null`。

```java
empMapper.updateByPrimaryKey(new Emp());【会加上 null】
empMapper.updateByPrimaryKeySelective(new Emp(1, "张三", null, null, null, null));【不会加上 null】
```

## `MyBatis`分页功能

`MyBatis`可以通过`pageHelper`依赖然后在核心配置文件中添加分页插件来实现分页功能。

1. `pom.xml`中添加分页功能的依赖

   ```xml
   <dependency>
       <groupId>com.github.pagehelper</groupId>
       <artifactId>pagehelper</artifactId>
       <version>5.3.1</version>
   </dependency>
   ```

2. 在`MyBatis`核心配置文件中配置分页插件

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
       <properties resource="jdbc.properties"></properties>
       <typeAliases>
           <package name="com.kk.mapper"/>
       </typeAliases>
       <plugins>
           <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
       </plugins>
       <environments default="mysql">
           <environment id="mysql">
               <transactionManager type="JDBC"></transactionManager>
               <dataSource type="POOLED">
                   <property name="driver" value="${jdbc.driver}"/>
                   <property name="url" value="${jdbc.url}"/>
                   <property name="username" value="${jdbc.username}"/>
                   <property name="password" value="${jdbc.password}"/>
               </dataSource>
           </environment>
       </environments>
       <mappers>
           <package name="com.kk.mapper"/>
       </mappers>
   </configuration>

3. 分页插件的使用

   `limit(index, pageSize)`：**`index`表示的是当前页的起始索引，`pageSize`表示的是每页显示的条数**

   `pageNum`：表示当前页的页码。

   我们现在只有`pageSize`每页显示的条数和`pageNum`即当前页，如何计算出当前页的起始索引`index`呢？ ---> `index = (pageNum - 1) * pageSize`【第一页索引是从`0`开始的，假设每页有`10`条数据，则第二页索引为`10`，那么索引就很好计算了：`(pageNum - 1) * pageSize`即当前页减去`1`乘以`pageSize`每页显示的条数就是起始索引】

   **`pageInfo`就是分页相关信息。**

   ```java
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
   ```
