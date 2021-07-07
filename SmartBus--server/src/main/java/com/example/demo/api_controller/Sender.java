package com.example.demo.api_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Sender {
    @RequestMapping(value = "/send")
    public String send(){
        return "Test!";
    }

}
