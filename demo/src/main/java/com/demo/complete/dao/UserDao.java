package com.demo.complete.dao;


import com.demo.complete.domain.User;

public interface UserDao {

	User queryUserById(User param);

	User findUserById(User param);

	int updateByUserById(User param);

	int insertByUser(User param);

	int deleteByUserById(User param);
}
