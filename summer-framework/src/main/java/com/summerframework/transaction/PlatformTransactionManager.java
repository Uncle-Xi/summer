package com.summerframework.transaction;


import java.sql.Connection;

public interface PlatformTransactionManager {

    void commit(Connection connection) throws RuntimeException;

    void rollback(Connection connection) throws RuntimeException;
}
