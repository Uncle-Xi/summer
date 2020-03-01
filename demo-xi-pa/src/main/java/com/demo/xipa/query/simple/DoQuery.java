package com.demo.xipa.query.simple;

import com.demo.xipa.domain.User;
import com.demo.xipa.parse.xml.ParseXml;
import com.xipa.io.Resources;
import com.xipa.session.SqlSession;
import com.xipa.session.SqlSessionFactory;
import com.xipa.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @description: do query
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DoQuery {

    private static final String resource = "xipa-config.xml";
    private static final Logger log = Logger.getLogger(ParseXml.class.toString());

    public static void main(String[] args) throws IOException, SQLException {

        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
//        System.out.println(dataSource.toString());

        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setId(1);
        user.setUsername("牛逼哄哄要上天");
        String statementId = "com.demo.xipa.dao.UserDao" + "." + "findUserById";
        Object o = sqlSession.selectOne(statementId, user);
        System.out.println(o);
        System.out.println("-------------------");

        System.out.println("------------------");
        String update = "com.demo.xipa.dao.UserDao" + "." + "updateByUserById";
        user.setId(3);
        user.setUsername("就是这么掉渣");
        int i = sqlSession.update(update, user);
        System.out.println(i);
    }
}
