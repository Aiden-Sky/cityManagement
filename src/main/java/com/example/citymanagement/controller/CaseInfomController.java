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
@RequestMapping("/caseInfom")
public class CaseInfomController {
    @Autowired
    private CaseInfomService caseInfomService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private View error;

    @GetMapping("/getInfoms")
    @ResponseBody
    public ResponseEntity<ReportResponse> getReports(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestHeader("Authorization") String token) {

        // 验证token是否有SystemAdmin权限
        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 获取案例信息
        List<CaseInfom> caseInfomList = caseInfomService.getReports(page, pageSize);
        int totalPages = caseInfomService.getTotalPages(pageSize);

        // 创建响应对象
        ReportResponse response = new ReportResponse();
        response.setReports(caseInfomList);
        response.setTotalPages(totalPages);

        return ResponseEntity.ok(response);
    }

    // 管理员设置/更新案例信息
    @PostMapping("/SetInfom")
    @ResponseBody
    public ResponseEntity<String> setReports(@RequestBody CaseInfom reportData,
            @RequestHeader("Authorization") String token) {
        // 验证token是否有管理权限
        if (!jwtUtil.validateToken(token, "SystemAdmin") && !jwtUtil.validateToken(token, "Management")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        if (caseInfomService.SetReports(reportData)) {
            return ResponseEntity.ok("操作成功");
        } else {
            return ResponseEntity.status(500).body("操作失败");
        }
    }

    // 删除案例
    @PostMapping("/deletInfom")
    @ResponseBody
    public ResponseEntity<String> deletInfom(@RequestParam("caseId") int caseId,
            @RequestHeader("Authorization") String token) {
        // 验证token是否有管理权限
        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        if (caseInfomService.deleteCaseById(caseId)) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.status(404).body("删除失败：未找到该案件");
        }
    }
}
