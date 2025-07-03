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
    public EmployeePerformance getPerformanceById(@PathVariable int id) {
        return employeePerformanceService.getPerformanceById(id);
    }

    @PostMapping
    public ResponseEntity<String> addPerformance(@RequestBody EmployeePerformance employeePerformance) {
        boolean success = employeePerformanceService.addPerformance(employeePerformance);
        return success ? ResponseEntity.ok("添加成功") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("添加失败");
    }

    @PutMapping
    public ResponseEntity<String> updatePerformance(@RequestBody EmployeePerformance employeePerformance) {
        boolean success = employeePerformanceService.updatePerformance(employeePerformance);
        return success ? ResponseEntity.ok("更新成功") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerformance(@PathVariable int id) {
        boolean success = employeePerformanceService.deletePerformance(id);
        return success ? ResponseEntity.ok("删除成功") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败");
    }
}
