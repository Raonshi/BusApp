package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    //클라이언트에서 IMEI를 받아 유저로 등록
    @RequestMapping(method = RequestMethod.GET, path ="/createUser")
    public String createUser(@RequestParam String imei) {
        UserDto userDto = new UserDto();
        userDto.setIMEI_id(imei);

        userService.createUser(userDto);

        return imei;
    }
}
