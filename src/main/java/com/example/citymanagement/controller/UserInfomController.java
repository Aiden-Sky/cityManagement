package com.example.citymanagement.controller;

import com.example.citymanagement.entity.Manager;
import com.example.citymanagement.service.UserInfomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class UserInfomController {
    @Autowired
    private UserInfomService userInfomService;

    @GetMapping("/getmanagers")
    @ResponseBody
    public List<Manager> getManagers() {
        return userInfomService.getAllManagers();
    }


}
