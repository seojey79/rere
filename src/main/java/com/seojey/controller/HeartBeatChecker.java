package com.seojey.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kimjun on 16. 5. 16..
 */
@Controller
public class HeartBeatChecker {
    @RequestMapping(value = "/heartbeat")
    @ResponseBody
    public String heartbeat() {
        return  "alive..";
    }

}
