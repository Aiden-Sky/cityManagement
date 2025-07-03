package com.example.citymanagement.service;

import com.example.citymanagement.Dao.EmployeePerformanceMapper;
import com.example.citymanagement.entity.EmployeePerformance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
