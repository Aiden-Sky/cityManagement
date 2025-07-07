package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.CaseInfom;

import java.util.List;

public interface CaseInfomMapper {

    List<CaseInfom> getReports(int offset, int pageSize);

    List<CaseInfom> selectAllCaseInfom();

    int getTotalCase();

    int insertCaseInfom(CaseInfom caseInfom);

    int updateCaseInfom(CaseInfom caseInfom);

    int deleteCaseById(int caseId);

    /**
     * 根据上报者查询案件
     * 
     * @param reporter 上报者用户名
     * @param offset   偏移量
     * @param pageSize 每页大小
     * @return 案件列表
     */
    List<CaseInfom> getReportsByReporter(String reporter, int offset, int pageSize);

    /**
     * 根据案件ID获取案件详情
     * 
     * @param caseId 案件ID
     * @return 案件详情
     */
    CaseInfom getCaseById(int caseId);
}
