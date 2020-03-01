package com.demo.xipa.dao;

import com.demo.xipa.domain.User;

public interface UserDao {

	User queryUserById(User param);

	User findUserById(User param);

	int updateByUserById(User param);

	int insertByUser(User param);

	int deleteByUserById(User param);
}
