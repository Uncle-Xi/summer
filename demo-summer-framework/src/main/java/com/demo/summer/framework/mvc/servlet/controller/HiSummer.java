package com.demo.summer.framework.mvc.servlet.controller;

import com.demo.summer.framework.mvc.servlet.service.HiService;
import com.demo.summer.framework.mvc.servlet.service.impl.CglibService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.stereotype.Controller;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RequestParam;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @description: HiSummer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Controller
public class HiSummer{

    private static final Logger logger = Logger.getLogger(HiSummer.class.toString());

    @Autowired
    HiService hiService;

    @Autowired
    CglibService cglibService;

    @GetMapping("/")
    public String hiSummer(HttpServletResponse resp, @RequestParam("msg") String msg){
        logger.info("hiSummer 处理开始：");
        logger.info("HiService 处理消息结果：" + hiService.messageHandler(msg));
        logger.info("cglibService 处理消息结果：");
        cglibService.cglibHandler(msg);
        return "Received your message: [" + msg + "], Welcome use Summer.";
    }

    @GetMapping("/hiSummer2")
    public ModelAndView hiSummer2(HttpServletResponse resp, @RequestParam(value = "msg") String msg){
        logger.info("HiService 处理消息结果：" + hiService.messageHandler(msg));
        Map<String, String> model = new HashMap<>();
        model.put("饥渴", "难耐");
        model.put("优秀", "帅气");
        model.put("一表", "人才");
        ModelAndView modelAndView = new ModelAndView("json", model);
        return modelAndView;
    }

    @GetMapping("/hiSummer3")
    public ModelAndView hiSummer3(HttpServletResponse resp, @RequestParam(value = "msg") String msg){
        logger.info("HiService 处理消息结果：" + hiService.messageHandler(msg));
        Map<String, String> model = new HashMap<>();
        model.put("饥渴", "难耐");
        ModelAndView modelAndView = new ModelAndView("index", model);
        return modelAndView;
    }

    public void sayHi(){
        System.out.println("循环依赖 cycle dependence is OK.");
    }
}
