package com.example.citymanagement.Dto;

import com.example.citymanagement.entity.CaseInfom;

import java.time.LocalDateTime;

/**
 * 案件上报DTO，用于接收前端的案件上报数据
 */
public class CaseReportDTO {
    private int caseID;
    private String reporterID;
    private String photoUrl;
    private String caseType;
    private String description;
    private String location;
    private String locationDescribe;
    private String status;
    private String createdDate;
    private String closedDate;
    private String reporter;
    private String reporterPhone;
    private boolean needResponse;
    private String infoCategory;
    private String handlingMethod;
    private boolean verified;

    /**
     * 将DTO转换为实体类
     * 
     * @return CaseInfom实体
     */
    public CaseInfom toCaseInfom() {
        CaseInfom caseInfom = new CaseInfom();
        caseInfom.setCaseID(this.caseID);
        // 处理photoUrl，处理Base64格式的图片数据
        if (this.photoUrl != null && !this.photoUrl.isEmpty()) {
            // 检查是否是data:image开头的Base64编码
            if (this.photoUrl.startsWith("data:image")) {
                // 提取Base64部分
                String base64Image = this.photoUrl.split(",")[1];
                // 存储原始字节数组
                caseInfom.setPhoto(java.util.Base64.getDecoder().decode(base64Image));
            } else {
                // 如果不是Base64格式，直接存储字符串
                caseInfom.setPhoto(this.photoUrl.getBytes());
            }
        }
        caseInfom.setCaseType(this.caseType);
        caseInfom.setDescription(this.description);
        caseInfom.setLocation(this.location);
        caseInfom.setLocationDescribe(this.locationDescribe);
        caseInfom.setStatus(this.status);

        // 日期处理
        if (this.createdDate != null && !this.createdDate.isEmpty()) {
            try {
                // 实际项目中应该使用合适的日期格式解析
                // 这里简单处理，使用当前时间
                caseInfom.setCreatedDate(LocalDateTime.now());
            } catch (Exception e) {
                caseInfom.setCreatedDate(LocalDateTime.now());
            }
        }

        if (this.closedDate != null && !this.closedDate.isEmpty()) {
            try {
                // 实际项目中应该使用合适的日期格式解析
                caseInfom.setClosedDate(LocalDateTime.now());
            } catch (Exception e) {
                // 忽略错误，保持为null
            }
        }

        caseInfom.setReporter(this.reporter);
        caseInfom.setReporterPhone(this.reporterPhone);
        caseInfom.setNeedResponse(this.needResponse);
        caseInfom.setInfoCategory(this.infoCategory);
        caseInfom.setHandlingMethod(this.handlingMethod);
        caseInfom.setVerified(this.verified);

        return caseInfom;
    }

    // Getters and Setters
    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public boolean isNeedResponse() {
        return needResponse;
    }

    public void setNeedResponse(boolean needResponse) {
        this.needResponse = needResponse;
    }

    public String getInfoCategory() {
        return infoCategory;
    }

    public void setInfoCategory(String infoCategory) {
        this.infoCategory = infoCategory;
    }

    public String getHandlingMethod() {
        return handlingMethod;
    }

    public void setHandlingMethod(String handlingMethod) {
        this.handlingMethod = handlingMethod;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getReporterID() {
        return reporterID;
    }

    public void setReporterID(String reporterID) {
        this.reporterID = reporterID;
    }
}