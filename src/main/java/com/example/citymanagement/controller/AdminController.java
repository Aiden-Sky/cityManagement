package com.example.citymanagement.controller;

import com.example.citymanagement.entity.Admin;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.service.AdminService;
import com.example.citymanagement.service.UserService;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 管理员登录 - 修改为直接返回token字符串
    @PostMapping("/login")
    @ResponseBody
    public String adminLogin(@RequestParam String account, @RequestParam String password) {
        // 处理登录逻辑
        String token = userService.authenticate(account, password);
        if (token != null) {
            // 验证是否是管理员
            User user = userService.getUserByAccount(account);
            if (user != null && "SystemAdmin".equals(user.getUserType())) {
                // 直接返回token字符串，符合前端期望
                return token;
            } else {
                throw new RuntimeException("该账号不是管理员账号");
            }
        } else {
            throw new RuntimeException("账号或密码不正确");
        }
    }

    // 获取所有管理员
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Admin>> getAllAdmins(@RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // 根据ID获取管理员信息
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Admin> getAdminById(@PathVariable int id, @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取当前登录管理员的信息
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<Admin> getAdminInfo(@RequestHeader("Authorization") String token) {
        try {
            // 验证超级管理员权限
            if (!validateAdminToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // 从token中获取用户名
            String username = jwtUtil.getUsernameFromToken(token);
            Admin admin = adminService.getAdminByAccount(username);

            if (admin != null) {
                return ResponseEntity.ok(admin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 添加新管理员
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin, @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        try {
            if (adminService.addAdmin(admin)) {
                return ResponseEntity.ok("管理员添加成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("管理员添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("管理员添加失败：" + e.getMessage());
        }
    }

    // 更新管理员信息
    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateAdmin(@RequestBody Admin admin, @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        if (adminService.updateAdmin(admin)) {
            return ResponseEntity.ok("管理员信息更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("管理员信息更新失败");
        }
    }

    // 重置管理员密码
    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        String account = request.get("account");
        String newPassword = request.get("newPassword");

        if (account == null || newPassword == null) {
            return ResponseEntity.badRequest().body("账号和新密码不能为空");
        }

        if (adminService.resetAdminPassword(account, newPassword)) {
            return ResponseEntity.ok("密码重置成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("密码重置失败");
        }
    }

    // 删除管理员
    @DeleteMapping("/delete/{account}")
    @ResponseBody
    public ResponseEntity<String> deleteAdmin(@PathVariable String account,
            @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 防止删除自己
        String currentUsername = jwtUtil.getUsernameFromToken(token);
        if (account.equals(currentUsername)) {
            return ResponseEntity.badRequest().body("不能删除当前登录的管理员账号");
        }

        if (adminService.deleteAdmin(account)) {
            return ResponseEntity.ok("管理员删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("管理员删除失败");
        }
    }

    // 辅助方法：验证是否是管理员token
    private boolean validateAdminToken(String token) {
        return jwtUtil.validateToken(token, "SystemAdmin");
    }

    // 处理异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}