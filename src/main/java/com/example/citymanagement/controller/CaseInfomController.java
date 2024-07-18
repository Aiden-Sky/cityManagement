package com.example.citymanagement.controller;

import com.example.citymanagement.Dto.ReportResponse;
import com.example.citymanagement.entity.CaseInfom;
import com.example.citymanagement.service.CaseInfomService;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class CaseInfomController {
    @Autowired
    private CaseInfomService caseInfomService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private View error;

    @GetMapping("/caseInfom/getInfoms")
    @ResponseBody
    public ResponseEntity<ReportResponse> getReports(@RequestParam int page, @RequestParam int pageSize, @RequestHeader("Authorization") String token) {

        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
//创建返回
        ReportResponse response = new ReportResponse();
//        int pageSize = 10;
//        int totalReports = caseInfomService.getTotalReports();
//        int totalPages = (int) Math.ceil((double) totalReports / pageSize);

        List<CaseInfom> caseInfomList = caseInfomService.getReports(page, pageSize);
        int totalPages = caseInfomService.getTotalPages(pageSize);

        response.setReports(caseInfomList);
        response.setTotalPages(totalPages);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/caseInfom/SetInfom")
    @ResponseBody
    public ResponseEntity<String> SetReports( @RequestBody CaseInfom reportData) {

        if (caseInfomService.SetReports(reportData)) {
            return ResponseEntity.ok("添加成功");
        } else {
            return ResponseEntity.status(401).body("添加失败");
        }


    }
}
