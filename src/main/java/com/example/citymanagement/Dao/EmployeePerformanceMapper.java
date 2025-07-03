package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.EmployeePerformance;

import java.util.List;

public interface EmployeePerformanceMapper {
     List<EmployeePerformance> findAll();
    EmployeePerformance findById(int id);
    int insert(EmployeePerformance employeePerformance);
    int update(EmployeePerformance employeePerformance);
    int delete(int id);
}
