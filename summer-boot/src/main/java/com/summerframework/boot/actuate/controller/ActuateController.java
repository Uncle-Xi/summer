package com.summerframework.boot.actuate.controller;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.boot.actuate.mbean.HealthMBean;
import com.summerframework.boot.actuate.configuration.ActuateProperties;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RestController;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: ActuateController
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@RestController(value = "/actuate")
public class ActuateController {

    @Autowired
    HealthMBean health;

    @Autowired
    ActuateProperties properties;

    /**
     * http://localhost:8866/actuate/info#
     * @return
     */
    @GetMapping(value = "/info")
    public ModelAndView info(){
        Map<String, Object> model = new HashMap<>();
        ModelAndView mv = new ModelAndView("json", model);
        if (properties == null || !properties.getEnable().equalsIgnoreCase("true")) {
            return mv;
        }
        model.put("status", health.getStatus());
        model.put("actuateProperties", properties);
        model.putAll(health.getJmxMBeanMap());
        mv = new ModelAndView("json", model);
        return mv;
    }
}
