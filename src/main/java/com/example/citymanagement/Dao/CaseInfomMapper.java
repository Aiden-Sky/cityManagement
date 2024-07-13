package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.CaseInfom;

import java.util.List;

public interface CaseInfomMapper {

    CaseInfom selectCaseInfomById(int id);

    List<CaseInfom> selectAllCaseInfom();

    int insertCaseInfom(CaseInfom caseInfom);

    int updateCaseInfom(CaseInfom caseInfom);

    int deleteCaseInfomById(int id);

}
