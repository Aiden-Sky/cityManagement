package com.example.citymanagement.controller;

import com.example.citymanagement.entity.Manager;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.service.ManagerService;
import com.example.citymanagement.service.UserService;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/city/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取所有城市管理者
     */
    @GetMapping("/all")
    public ResponseEntity<List<Manager>> getAllManagers(@RequestHeader("Authorization") String token) {
        // 验证系统管理员权限
        if (!jwtUtil.validateToken(token, "SysAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(managerService.getAllManagers());
    }

    /**
     * 获取指定ID的城市管理者
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getManagerById(@PathVariable int id, @RequestHeader("Authorization") String token) {
        // 验证系统管理员权限
        if (!jwtUtil.validateToken(token, "SysAdmin") &&
                !jwtUtil.validateToken(token, "Management")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 如果是城市管理者，只能查看自己的信息
        if (jwtUtil.validateToken(token, "Management")) {
            String username = jwtUtil.getUsernameFromToken(token);
            Manager manager = managerService.getManagerByAccount(username);
            if (manager == null || manager.getManageID() != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限查看其他管理者信息");
            }
        }

        Manager manager = managerService.getManagerById(id);
        if (manager != null) {
            return ResponseEntity.ok(manager);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到ID为" + id + "的城市管理者");
        }
    }

    /**
     * 获取当前登录管理者信息
     */
    @GetMapping("/info")
    public ResponseEntity<?> getManagerInfo(@RequestHeader("Authorization") String token) {
        // 验证城市管理者权限
        if (!jwtUtil.validateToken(token, "Management")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        String username = jwtUtil.getUsernameFromToken(token);
        Manager manager = managerService.getManagerByAccount(username);

        if (manager != null) {
            return ResponseEntity.ok(manager);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到城市管理者信息");
        }
    }

    /**
     * 添加新城市管理者
     */
    @PostMapping("/add")
    public ResponseEntity<String> addManager(@RequestBody Manager manager,
            @RequestParam(required = false) String password, @RequestHeader("Authorization") String token) {
        // 验证系统管理员权限
        if (!jwtUtil.validateToken(token, "SysAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        try {
            // 检查账号是否已存在
            User existingUser = userService.getUserByAccount(manager.getAccount());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("账号已存在");
            }

            // 如果未提供密码，使用默认密码
            String managerPassword = (password != null && !password.isEmpty()) ? password : "123456";

            // 添加城市管理者，并传入密码
            boolean success = managerService.addManagerWithPassword(manager, managerPassword);
            if (success) {
                return ResponseEntity.ok("城市管理者添加成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("城市管理者添加失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("城市管理者添加失败：" + e.getMessage());
        }
    }

    /**
     * 更新城市管理者信息
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateManager(@RequestBody Manager manager,
            @RequestParam(required = false) String password,
            @RequestHeader("Authorization") String token) {
        // 验证权限
        boolean isAdmin = jwtUtil.validateToken(token, "SysAdmin");
        boolean isManager = jwtUtil.validateToken(token, "Management");

        if (!isAdmin && !isManager) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 如果是城市管理者，只能修改自己的信息
        if (isManager) {
            String username = jwtUtil.getUsernameFromToken(token);
            Manager existingManager = managerService.getManagerByAccount(username);
            if (existingManager == null || existingManager.getManageID() != manager.getManageID()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限修改其他管理者信息");
            }
        }

        try {
            boolean success;

            // 如果提供了新密码，则更新密码
            if (password != null && !password.isEmpty()) {
                success = managerService.updateManagerWithPassword(manager, password);
            } else {
                success = managerService.updateManager(manager);
            }

            if (success) {
                return ResponseEntity.ok("城市管理者信息更新成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("城市管理者信息更新失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("城市管理者信息更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除城市管理者
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable int id, @RequestHeader("Authorization") String token) {
        // 验证系统管理员权限
        if (!jwtUtil.validateToken(token, "SysAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        try {
            boolean success = managerService.deleteManager(id);
            if (success) {
                return ResponseEntity.ok("城市管理者删除成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("城市管理者删除失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("城市管理者删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据部门获取城市管理者列表
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Manager>> getManagersByDepartment(@PathVariable String department,
            @RequestHeader("Authorization") String token) {
        // 验证权限
        if (!jwtUtil.validateToken(token, "SysAdmin") &&
                !jwtUtil.validateToken(token, "Management")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Manager> managers = managerService.getManagersByDepartment(department);
        return ResponseEntity.ok(managers);
    }

    /**
     * 重置城市管理者密码
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        // 验证系统管理员权限
        if (!jwtUtil.validateToken(token, "SysAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        String account = request.get("account");
        String newPassword = request.get("newPassword");

        if (account == null || newPassword == null) {
            return ResponseEntity.badRequest().body("账号和新密码不能为空");
        }

        try {
            boolean success = managerService.resetManagerPassword(account, newPassword);
            if (success) {
                return ResponseEntity.ok("密码重置成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("密码重置失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("密码重置失败：" + e.getMessage());
        }
    }
}