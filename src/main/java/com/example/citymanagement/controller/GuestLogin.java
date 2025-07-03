package com.example.citymanagement.controller;

import com.example.citymanagement.Dto.ReportResponse;
import com.example.citymanagement.entity.CaseInfom;
import com.example.citymanagement.service.CaseInfomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/guest")
public class GuestLogin {

    @Autowired
    private CaseInfomService caseInfomService;

    // 游客查看公开的案例信息，不需要登录验证
    @GetMapping("/cases")
    @ResponseBody
    public ResponseEntity<ReportResponse> getPublicCases(@RequestParam int page, @RequestParam int pageSize) {
        List<CaseInfom> caseInfomList = caseInfomService.getReports(page, pageSize);
        int totalPages = caseInfomService.getTotalPages(pageSize);

        ReportResponse response = new ReportResponse();
        response.setReports(caseInfomList);
        response.setTotalPages(totalPages);

        return ResponseEntity.ok(response);
    }

    // 游客提交新案例
    @PostMapping("/submit-case")
    @ResponseBody
    public ResponseEntity<String> submitCase(@RequestBody CaseInfom caseInfom) {
        // 设置为未验证状态
        caseInfom.setVerified(false);
        // 设置默认状态为"待处理"
        caseInfom.setStatus("待处理");

        if (caseInfomService.SetReports(caseInfom)) {
            return ResponseEntity.ok("案例提交成功，等待审核");
        } else {
            return ResponseEntity.status(500).body("案例提交失败");
        }
    }
}
