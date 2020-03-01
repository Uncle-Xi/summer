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
import java.util.List;
import java.util.logging.Logger;

/**
 * @description: do query
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class QueryBySqlSession {

    private static final Logger logger = Logger.getLogger(QueryBySqlSession.class.toString());
    private static final String RESOURCE = "xipa-config.xml";
    private static final String STATEMENT_ID_PREFIX = "com.demo.xipa.dao.UserDao" + ".";
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

    public static void main(String[] args) throws Exception {
        System.out.println("--------------------------【华丽的分割线】-------------------------");
        User user = new User();
        user.setId(1);
        User dbUser = sqlSession.selectOne(STATEMENT_ID_PREFIX + "findUserById", user);
        logger.info(dbUser.toString());

        System.out.println("--------------------------【华丽的分割线】-------------------------");
        user = new User();
        user.setId(3);
        user.setUsername("帅的掉渣");
        int changeNum = sqlSession.update(STATEMENT_ID_PREFIX + "updateByUserById", user);
        logger.info("【修改影响条数】-【" + changeNum);
    }
}
