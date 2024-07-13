package com.example.citymanagement.service;

import com.example.citymanagement.Dao.CaseInfomMapper;
import com.example.citymanagement.entity.CaseInfom;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseInfomService {
    @Autowired(required = false)
    private CaseInfomMapper caseInfomMapper;
    @Autowired
    private JwtUtil jwtUtil;


    public List<CaseInfom> getAllInfoms() {

    }
}
