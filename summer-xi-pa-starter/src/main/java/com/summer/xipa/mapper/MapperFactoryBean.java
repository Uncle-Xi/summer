package com.summer.xipa.mapper;

import com.summerframework.beans.FactoryBean;
import com.xipa.session.SqlSession;

/**
 * @description: MapperFactoryBean
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperFactoryBean<T> implements FactoryBean {

    private Class<T> mapperInterface;
    private SqlSession sqlSession;

    public MapperFactoryBean(SqlSession sqlSession, Class<T> mapperInterface){
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    public T getMapper() {
        return sqlSession.getMapper(this.mapperInterface);
    }
}
