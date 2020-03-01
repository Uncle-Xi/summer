package com.demo.summer.boot.controller;

import com.demo.summer.boot.service.ConstructorService;
import com.demo.summer.boot.service.HiBootService;
import com.demo.summer.boot.service.impl.BootCglibService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Controller;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RequestParam;
import com.summerframework.web.protocol.http.HttpServerResponse;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: HiSummer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Controller
public class HiSummerBoot {

    private static final LogFactory logger = new LogFactory(HiSummerBoot.class);

    @Autowired
    HiBootService hiBootService;

    @Autowired
    BootCglibService bootCglibService;

    @Autowired
    ConstructorService constructorService;

    @GetMapping("/")
    public String index(HttpServerResponse resp, @RequestParam("msg") String msg){
        logger.info("【/】【调用】【HiBootService】 - [" + hiBootService.messageHandler(msg));
        logger.info("【/】【调用】【BootCglibService】 - [");
        bootCglibService.cglibHandler(msg);
        return "Welcome use Summer【Call】【HiBootService】【BootCglibService】【SUCCESS】";
    }

    @GetMapping("/hiBoot")
    public String hiBoot(HttpServerResponse resp, @RequestParam("msg") String msg){
        logger.info("【/hiBoot】【调用】【ConstructorService】 - [");
        constructorService.constructorInvoke();
        logger.info("【/hiBoot】【调用】【BootCglibService】 - [");
        bootCglibService.cglibHandler(msg);
        return "Welcome use Summer【Call】【ConstructorService】【BootCglibService】【SUCCESS】";
    }

    @GetMapping("/hiBoot2")
    public ModelAndView hiBoot2(HttpServerResponse resp, @RequestParam(value = "msg") String msg){
        logger.info("【/hiBoot2】【调用】【HiBootService】 - [" + hiBootService.messageHandler(msg));
        Map<String, String> model = new HashMap<>();
        model.put("测试", "效果");
        model.put("饥渴", "难耐");
        model.put("优秀", "帅气");
        model.put("一表", "人才");
        ModelAndView modelAndView = new ModelAndView("json", model);
        return modelAndView;
    }

    @GetMapping("/hiBoot3")
    public ModelAndView hiBoot3(HttpServerResponse resp, @RequestParam(value = "msg") String msg){
        logger.info("【/hiBoot3】【调用】【HiBootService】 - [" + hiBootService.messageHandler(msg));
        Map<String, String> model = new HashMap<>();
        model.put("饥渴", "难耐");
        ModelAndView modelAndView = new ModelAndView("index", model);
        return modelAndView;
    }

    public void sayHi(){
        logger.info("循环依赖 cycle dependence is OK.");
    }
}
