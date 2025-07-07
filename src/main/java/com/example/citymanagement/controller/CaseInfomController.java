package com.example.citymanagement.controller;

import com.example.citymanagement.Dto.CaseReportDTO;
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

import java.time.LocalDateTime;
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

    // 居民上报案件
    @PostMapping("/reportCase")
    @ResponseBody
    public ResponseEntity<String> reportCase(@RequestBody CaseReportDTO caseReportDTO,
            @RequestHeader("Authorization") String token) {
        // 验证token是否为居民权限
        if (!jwtUtil.validateToken(token, "Resident")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 获取居民信息
        String username = jwtUtil.getUsernameFromToken(token);

        // 将DTO转换为实体
        CaseInfom caseInfom = caseReportDTO.toCaseInfom();

        // 设置案件初始状态
        caseInfom.setStatus("待处理");
        caseInfom.setCreatedDate(LocalDateTime.now());
        caseInfom.setVerified(false);
        caseInfom.setReporter(username);

        // 保存案件信息
        if (caseInfomService.SetReports(caseInfom)) {
            return ResponseEntity.ok("案件上报成功");
        } else {
            return ResponseEntity.status(500).body("案件上报失败");
        }
    }

    // 居民查询自己上报的案件
    @GetMapping("/getMyReports")
    @ResponseBody
    public ResponseEntity<List<CaseInfom>> getMyReports(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestHeader("Authorization") String token) {

        // 验证token是否为居民权限
        if (!jwtUtil.validateToken(token, "Resident")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 获取居民用户名
        String username = jwtUtil.getUsernameFromToken(token);

        // 获取该居民上报的案件
        List<CaseInfom> myCases = caseInfomService.getReportsByUsername(username, page, pageSize);

        return ResponseEntity.ok(myCases);
    }

    // 获取案件详情
    @GetMapping("/getCaseDetail")
    @ResponseBody
    public ResponseEntity<CaseInfom> getCaseDetail(
            @RequestParam int caseId,
            @RequestHeader("Authorization") String token) {

        // 验证token
        String userType = jwtUtil.getUserTypeFromToken(token);
        if (userType == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 获取案件详情
        CaseInfom caseDetail = caseInfomService.getCaseById(caseId);

        // 如果是居民，需要验证是否是自己上报的案件
        if ("Resident".equals(userType)) {
            String username = jwtUtil.getUsernameFromToken(token);
            if (!username.equals(caseDetail.getReporter())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        return ResponseEntity.ok(caseDetail);
    }
}
