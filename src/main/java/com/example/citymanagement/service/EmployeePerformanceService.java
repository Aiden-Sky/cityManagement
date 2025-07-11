package com.example.citymanagement.service;

import com.example.citymanagement.Dao.EmployeePerformanceMapper;
import com.example.citymanagement.entity.EmployeePerformance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeePerformanceService {
    @Autowired
    private EmployeePerformanceMapper employeePerformanceMapper;

    public List<EmployeePerformance> getAllPerformances() {
        return employeePerformanceMapper.findAll();
    }

    public EmployeePerformance getPerformanceById(int id) {
        return employeePerformanceMapper.findById(id);
    }

    public boolean addPerformance(EmployeePerformance employeePerformance) {
        return employeePerformanceMapper.insert(employeePerformance) > 0;
    }

    public boolean updatePerformance(EmployeePerformance employeePerformance) {
        return employeePerformanceMapper.update(employeePerformance) > 0;
    }

    public boolean deletePerformance(int id) {
        return employeePerformanceMapper.delete(id) > 0;
    }

    // 根据部门获取绩效记录
    public List<EmployeePerformance> getPerformancesByDepartment(String department) {
        // 如果Mapper中没有直接提供按部门查询的方法，可以在服务层进行过滤
        List<EmployeePerformance> allPerformances = employeePerformanceMapper.findAll();
        return allPerformances.stream()
                .filter(p -> p.getDepartment() != null && p.getDepartment().equals(department))
                .collect(Collectors.toList());

        // 如果有更高性能要求，建议直接在Mapper层实现按部门查询
        // return employeePerformanceMapper.findByDepartment(department);
    }

    // 计算部门的平均评分
    public double getDepartmentAverageScore(String department) {
        List<EmployeePerformance> departmentPerformances = getPerformancesByDepartment(department);
        if (departmentPerformances.isEmpty()) {
            return 0;
        }

        return departmentPerformances.stream()
                .mapToDouble(EmployeePerformance::getEvaluationScore)
                .average()
                .orElse(0);
    }

    // 获取所有部门的评分排名
    public List<String> getDepartmentRankings() {
        // 这里的实现会根据实际的部门结构和评分规则而变化
        // 这只是一个示例实现
        return getAllPerformances().stream()
                .collect(Collectors.groupingBy(
                        EmployeePerformance::getDepartment,
                        Collectors.averagingDouble(EmployeePerformance::getEvaluationScore)))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // 降序排列
                .map(e -> e.getKey() + ": " + String.format("%.2f", e.getValue()))
                .collect(Collectors.toList());
    }
}
