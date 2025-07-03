package com.example.citymanagement.entity;

import java.time.LocalDateTime;

public class EmployeePerformance {
     private Integer manageID;
    private String name;
    private String department;
    private String position;
    private Integer completedCases;
    private Double qualityScore;
    private Integer evaluationScore;
    private LocalDateTime evaluationDate;
    private String comments;
    private LocalDateTime reportDate;

    // Getters and Setters
    public Integer getManageID() {
        return manageID;
    }

    public void setManageID(Integer manageID) {
        this.manageID = manageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getCompletedCases() {
        return completedCases;
    }

    public void setCompletedCases(Integer completedCases) {
        this.completedCases = completedCases;
    }

    public Double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Double qualityScore) {
        this.qualityScore = qualityScore;
    }

    public Integer getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(Integer evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }
}
