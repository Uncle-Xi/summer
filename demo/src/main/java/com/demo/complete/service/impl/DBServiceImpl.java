package com.demo.complete.service.impl;

import com.demo.complete.dao.UserDao;
import com.demo.complete.domain.User;
import com.demo.complete.service.DBService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;

/**
 * @description: DBServiceImpl
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class DBServiceImpl implements DBService {

    private static final LogFactory logger = new LogFactory(DBServiceImpl.class);

    @Autowired
    private UserDao userDao;

    /**
     * http://localhost:8866/query?id=1
     *
     * @param id
     * @return
     */
    @Override
    public User queryUser(String id) {
        logger.info("【DBServiceImpl】【queryUser】【参数】-【" + id);
        User user = new User();
        user.setId(Integer.valueOf(id));
        return userDao.queryUserById(user);
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public int insertUser(User user) {
        logger.info("【DBServiceImpl】【insertUser】【OK】");
        return userDao.insertByUser(user);
    }
}
