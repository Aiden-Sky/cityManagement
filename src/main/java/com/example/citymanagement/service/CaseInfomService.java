package com.example.citymanagement.service;

import com.example.citymanagement.Dao.CaseInfomMapper;
import com.example.citymanagement.entity.CaseInfom;
import com.example.citymanagement.entity.Manager;
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


    public List<CaseInfom> getReports(int page, int pageSize) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        // 调用 DAO 层查询事件信息报告列表
        List<CaseInfom> caseInfoms = caseInfomMapper.getReports(offset, pageSize);
        return caseInfoms;
    }

    public int getTotalPages(int pageSize) {
        // 调用 DAO 层获取总报告数
        int totalCase = caseInfomMapper.getTotalCase();
        return (int) Math.ceil((double) totalCase / pageSize);
    }

    public boolean SetReports(CaseInfom caseinfom) {
        if (caseinfom.getCaseID()==0){
            int res = caseInfomMapper.insertCaseInfom(caseinfom);
            if (res > 0){return true;}
            return false;
        }
        else {
            int res = caseInfomMapper.updateCaseInfom(caseinfom);
            if (res > 0){return true;}
            return false;
        }

    }

    public boolean deleteCaseById(int caseId) {
        int result = caseInfomMapper.deleteCaseById(caseId);
        return result > 0; // 如果删除成功，返回true
    }



}
