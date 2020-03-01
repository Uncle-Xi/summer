package com.summer.xipa.mapper;

import com.xipa.session.SqlSession;
import com.xipa.session.SqlSessionFactory;

/**
 * @description: mapper support
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MapperSupport<T> {

    private Class<T> mapperInterface;
    private SqlSessionFactory sqlSessionFactory;

    public MapperSupport(SqlSessionFactory sqlSessionFactory, Class<T> mapperInterface){
        this.sqlSessionFactory = sqlSessionFactory;
        this.mapperInterface = mapperInterface;
    }

    public T getMapper(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.getMapper(this.mapperInterface);
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
