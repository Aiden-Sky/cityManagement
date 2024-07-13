package com.example.citymanagement.controller;

import com.example.citymanagement.entity.CaseInfom;
import com.example.citymanagement.service.CaseInfomService;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CaseInfomController {
    @Autowired
    private CaseInfomService caseInfomService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/caseIngom/getInfoms")
    @ResponseBody
    public List<CaseInfom> getAllInfoms() {
        List<CaseInfom> res = caseInfomService.getAllInfoms();
        return res;
    }



}
