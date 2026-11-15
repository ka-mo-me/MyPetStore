package org.csu.mypetstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @GetMapping(value = "/demo", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String demo() {
        return "Spring MVC 初始化成功123!";
    }
}
