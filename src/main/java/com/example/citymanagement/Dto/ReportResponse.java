package com.example.citymanagement.Dto;

import com.example.citymanagement.entity.CaseInfom;
import java.util.List;

public class ReportResponse {

    private List<CaseInfom> reports;
    private int totalPages;

    public List<CaseInfom> getReports() {
        return reports;
    }

    public void setReports(List<CaseInfom> reports) {
        this.reports = reports;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
