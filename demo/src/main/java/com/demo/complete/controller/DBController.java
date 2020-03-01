package com.demo.complete.controller;

import com.alibaba.fastjson.JSON;
import com.demo.complete.domain.User;
import com.demo.complete.service.DBService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RequestParam;
import com.summerframework.web.bind.annotation.RestController;
import com.summerframework.web.protocol.http.HttpServerResponse;
import com.ximq.common.util.StringUtils;

import java.util.UUID;

/**
 * @description: DataBaseController
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@RestController
public class DBController {

    private static final LogFactory logger = new LogFactory(DBController.class);

    @Autowired
    DBService dbService;
    /**
     * http://localhost:8866/query?id=1
     * @param resp
     * @param id
     * @return
     */
    @GetMapping("/query")
    public String query(HttpServerResponse resp, @RequestParam("id") String id){
        logger.info("【DBController】【query】【参数】-【" + id);
        if (id == null) {
            return "FAIL[id = null]";
        }
        return StringUtils.getString(dbService.queryUser(id));
    }

    /**
     * http://localhost:8866/insert?name=Xi&sex=Man&address=ChnegDu
     * @param name
     * @param sex
     * @param address
     * @return
     */
    @GetMapping("/insert")
    public String insert(@RequestParam("name") String name,
                         @RequestParam("sex") String sex,
                         @RequestParam("address") String address){
        logger.info("【DBController】【insert】【开始处理】");
        User user = new User();
        user.setId((int)System.currentTimeMillis());
        user.setAddress(address);
        user.setSex(sex);
        user.setUsername(name);
        return "【用户数据插入结果】 - 【" + dbService.insertUser(user);
    }
}
