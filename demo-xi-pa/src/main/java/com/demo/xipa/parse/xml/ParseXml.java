package com.demo.xipa.parse.xml;

import com.xipa.io.Resources;
import com.xipa.session.SqlSessionFactory;
import com.xipa.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @description: parse xml
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ParseXml {


    private static final Logger log = Logger.getLogger(ParseXml.class.toString());

    public static void main(String[] args) throws IOException {
        String resource = "xipa-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        System.out.println(dataSource.toString());
    }
}
