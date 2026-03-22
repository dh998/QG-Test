package com.dorm.newrepair.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;



public class MybatisUtil {
    //静态变量
    private static  SqlSessionFactory sqlSessionFactory;

    //加载mybatis的核心配置文件，获取SqlSessionFactory 来使用sql语句
    static {
        String resource = "mybatis-config.xml";
        InputStream inputStream;
        {
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }
        //获取sqlSession对象，用它来执行sql 同时设成自动提交事务
        public static SqlSession getSqlSession() {
            return sqlSessionFactory.openSession(true); // autoCommit: true
        }

    //释放资源
    public static void closeSqlSession(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}
