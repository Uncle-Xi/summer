package com.demo.xipa.query.proxy;

import com.demo.xipa.dao.UserDao;
import com.demo.xipa.domain.User;
import com.demo.xipa.query.simple.QueryBySqlSession;
import com.xipa.io.Resources;
import com.xipa.session.SqlSession;
import com.xipa.session.SqlSessionFactory;
import com.xipa.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @description: mapper proxy
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperProxy {

    private static final Logger logger = Logger.getLogger(QueryBySqlSession.class.toString());
    private static final String RESOURCE = "xipa-config.xml";
    private static SqlSession sqlSession;
    static {
        try {
            InputStream inputStream = Resources.getResourceAsStream(RESOURCE);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = sqlSessionFactory.openSession();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        User user = new User();
        user.setId(1);
        user.setUsername("牛逼哄哄要上天");
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        System.out.println(userDao.queryUserById(user).toString());
        System.out.println(userDao.findUserById(user).toString());
    }
}
