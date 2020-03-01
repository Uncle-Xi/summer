package com.demo.complete.controller;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Controller;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RequestParam;
import com.summerframework.web.protocol.http.HttpServerResponse;
import com.summerframework.web.webmvc.servlet.ModelAndView;
import com.demo.complete.service.DemoService;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: HiDemoController
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Controller
public class IndexController {

    @Autowired
    DemoService demoService;

    private static final LogFactory logger = new LogFactory(IndexController.class);

    @GetMapping("/")
    public ModelAndView index(){
        logger.info("【IndexController】【index】【Invoke】");
        String viewName = "index";
        Map<String, Object> model = new HashMap<>();
        model.put("msg", "已经收到您的请求，马上开始处理");
        ModelAndView mv = new ModelAndView(viewName, model);
        return mv;
    }

    @GetMapping("/hi")
    public String hi(HttpServerResponse resp, @RequestParam("msg") String msg){
        logger.info("【IndexController】【hi】【服务调用开始】");
        return demoService.hiDemo(msg);
    }
}
