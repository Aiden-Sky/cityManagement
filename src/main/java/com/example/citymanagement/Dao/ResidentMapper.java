package com.example.citymanagement.Dao;

import com.example.citymanagement.entity.Resident;
import java.util.List;

public interface ResidentMapper {
    // 根据账户名查询居民信息
    Resident getResidentByAccount(String account);

    // 查询所有居民信息
    List<Resident> getAllResidents();

    // 添加新居民
    int insertResident(Resident resident);

    // 更新居民信息
    int updateResident(Resident resident);

    // 删除居民信息
    int deleteResident(String account);

    // 根据居民ID查询居民信息
    Resident getResidentById(Long residentId);
}