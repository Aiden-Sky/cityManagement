package com.example.citymanagement.controller;

import com.example.citymanagement.entity.EmployeePerformance;
import com.example.citymanagement.service.EmployeePerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
public class EmployeePerformanceController {
    @Autowired
    private EmployeePerformanceService employeePerformanceService;

    @GetMapping
    public List<EmployeePerformance> getAllPerformances() {
        return employeePerformanceService.getAllPerformances();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPerformanceById(@PathVariable int id) {
        try {
            EmployeePerformance performance = employeePerformanceService.getPerformanceById(id);
            if (performance != null) {
                return ResponseEntity.ok(performance);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到ID为" + id + "的绩效记录");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取绩效记录失败: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> addPerformance(@RequestBody EmployeePerformance employeePerformance) {
        try {
            boolean success = employeePerformanceService.addPerformance(employeePerformance);
            return success ? ResponseEntity.ok("添加成功")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加失败");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加绩效记录失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updatePerformance(@RequestBody EmployeePerformance employeePerformance) {
        try {
            boolean success = employeePerformanceService.updatePerformance(employeePerformance);
            return success ? ResponseEntity.ok("更新成功")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新绩效记录失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerformance(@PathVariable int id) {
        try {
            boolean success = employeePerformanceService.deletePerformance(id);
            return success ? ResponseEntity.ok("删除成功")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除绩效记录失败: " + e.getMessage());
        }
    }

    // 根据部门获取绩效记录
    @GetMapping("/department/{department}")
    public ResponseEntity<?> getPerformancesByDepartment(@PathVariable String department) {
        try {
            List<EmployeePerformance> performances = employeePerformanceService.getPerformancesByDepartment(department);
            return ResponseEntity.ok(performances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取部门绩效记录失败: " + e.getMessage());
        }
    }

    // 获取绩效统计
    @GetMapping("/statistics")
    public ResponseEntity<?> getPerformanceStatistics() {
        try {
            // 这里可以返回各种统计信息，如各部门平均评分，完成案件总数等
            return ResponseEntity.ok("绩效统计功能未实现");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取绩效统计失败: " + e.getMessage());
        }
    }
}
