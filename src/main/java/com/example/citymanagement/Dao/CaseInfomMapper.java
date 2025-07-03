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
}
