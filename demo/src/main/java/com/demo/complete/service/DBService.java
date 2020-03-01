package com.demo.complete.service;

import com.demo.complete.domain.User;

public interface DBService {

    User queryUser(String id);

    int insertUser(User user);
}
