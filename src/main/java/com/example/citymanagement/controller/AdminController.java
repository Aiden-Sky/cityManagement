package com.example.citymanagement.controller;

import com.example.citymanagement.Dto.AdminInfoDTO;
import com.example.citymanagement.Dto.LoginHistoryDTO;
import com.example.citymanagement.Dto.MfaSetupDTO;
import com.example.citymanagement.Dto.MfaStatusDTO;
import com.example.citymanagement.Dto.MfaVerifyDTO;
import com.example.citymanagement.Dto.PasswordChangeDTO;
import com.example.citymanagement.entity.Admin;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.service.AdminService;
import com.example.citymanagement.service.LoginHistoryService;
import com.example.citymanagement.service.MfaService;
import com.example.citymanagement.service.UserService;
import com.example.citymanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/city/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private MfaService mfaService;

    @Autowired
    private JwtUtil jwtUtil;

    // 管理员登录 - 修改为直接返回token字符串
    @PostMapping("/login")
    @ResponseBody
    public String adminLogin(
            @RequestParam String account,
            @RequestParam String password) {

        // 处理登录逻辑
        String token = userService.authenticate(account, password);
        if (token != null) {
            // 验证是否是管理员
            User user = userService.getUserByAccount(account);
            if (user != null && "SysAdmin".equals(user.getUserType())) {
                // 记录登录成功
                recordLogin(account, "成功");

                // 直接返回token字符串，符合前端期望
                return token;
            } else {
                // 记录登录失败 - 非管理员角色
                recordLogin(account, "失败 - 非管理员角色");
                throw new RuntimeException("该账号不是管理员账号");
            }
        } else {
            // 记录登录失败 - 账号或密码错误
            recordLogin(account, "失败 - 账号或密码错误");
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

    // 获取当前登录管理员的信息 - 返回AdminInfoDTO
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<AdminInfoDTO> getAdminInfo(@RequestHeader("Authorization") String token) {
        try {
            // 验证超级管理员权限
            if (!validateAdminToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // 从token中获取用户名
            String username = jwtUtil.getUsernameFromToken(token);
            AdminInfoDTO adminInfoDTO = adminService.getAdminInfoDTO(username);

            if (adminInfoDTO != null) {
                return ResponseEntity.ok(adminInfoDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 更新管理员信息 - 使用AdminInfoDTO
    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateAdminInfo(@RequestBody AdminInfoDTO adminInfoDTO,
            @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 验证当前用户只能修改自己的信息
        String currentUsername = jwtUtil.getUsernameFromToken(token);
        if (!currentUsername.equals(adminInfoDTO.getAccount())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("只能修改自己的信息");
        }

        if (adminService.updateAdminInfo(adminInfoDTO)) {
            return ResponseEntity.ok("管理员信息更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("管理员信息更新失败");
        }
    }

    // 修改密码
    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO,
            @RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 获取当前用户名
        String username = jwtUtil.getUsernameFromToken(token);

        if (adminService.changePassword(username, passwordChangeDTO.getCurrentPassword(),
                passwordChangeDTO.getNewPassword())) {
            return ResponseEntity.ok("密码修改成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("当前密码错误或修改失败");
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
    @PutMapping("/update-admin")
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

    /**
     * 获取登录历史
     * 
     * @param token 认证令牌
     * @return 登录历史列表
     */
    @GetMapping("/loginHistory")
    @ResponseBody
    public ResponseEntity<List<LoginHistoryDTO>> getLoginHistory(@RequestHeader("Authorization") String token) {
        // 验证超级管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 获取账户名
        String account = jwtUtil.getUsernameFromToken(token);

        // 获取登录历史，默认获取10条
        List<LoginHistoryDTO> loginHistoryDTOs = loginHistoryService.getLoginHistory(account, 10);

        return ResponseEntity.ok(loginHistoryDTOs);
    }

    /**
     * 记录登录
     * 
     * @param account 账户名
     * @param status  状态 (成功/失败)
     */
    private void recordLogin(String account, String status) {
        try {
            // 获取当前请求的HttpServletRequest
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // 获取客户端IP
            String ip = getClientIP(request);

            // 获取设备信息
            String userAgent = request.getHeader("User-Agent");
            String device = userAgent != null ? userAgent : "未知设备";

            // 记录登录
            loginHistoryService.recordLogin(account, ip, device, status);
        } catch (Exception e) {
            // 记录登录失败不应该影响主要业务逻辑
            e.printStackTrace();
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获取MFA状态
     * 
     * @param token 认证令牌
     * @return MFA状态
     */
    @GetMapping("/mfa/status")
    @ResponseBody
    public ResponseEntity<MfaStatusDTO> getMfaStatus(@RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 获取账号
        String account = jwtUtil.getUsernameFromToken(token);

        // 获取MFA状态
        MfaStatusDTO mfaStatusDTO = mfaService.getMfaStatus(account);

        return ResponseEntity.ok(mfaStatusDTO);
    }

    /**
     * 设置MFA
     * 
     * @param token 认证令牌
     * @return MFA设置信息
     */
    @PostMapping("/mfa/setup")
    @ResponseBody
    public ResponseEntity<MfaSetupDTO> setupMfa(@RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 获取账号
        String account = jwtUtil.getUsernameFromToken(token);

        // 设置MFA
        MfaSetupDTO mfaSetupDTO = mfaService.setupMfa(account);

        return ResponseEntity.ok(mfaSetupDTO);
    }

    /**
     * 验证MFA并启用
     * 
     * @param mfaVerifyDTO 验证信息
     * @param token        认证令牌
     * @return 验证结果
     */
    @PostMapping("/mfa/verify")
    @ResponseBody
    public ResponseEntity<String> verifyMfa(@RequestBody MfaVerifyDTO mfaVerifyDTO,
            @RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 获取账号
        String account = jwtUtil.getUsernameFromToken(token);

        // 验证并启用MFA
        boolean verified = mfaService.verifyAndEnableMfa(account, mfaVerifyDTO.getCode());

        if (verified) {
            return ResponseEntity.ok("验证成功，MFA已启用");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码无效或已过期");
        }
    }

    /**
     * 禁用MFA
     * 
     * @param token 认证令牌
     * @return 操作结果
     */
    @PostMapping("/mfa/disable")
    @ResponseBody
    public ResponseEntity<String> disableMfa(@RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!validateAdminToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        // 获取账号
        String account = jwtUtil.getUsernameFromToken(token);

        // 禁用MFA
        boolean disabled = mfaService.disableMfa(account);

        if (disabled) {
            return ResponseEntity.ok("MFA已禁用");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("禁用MFA失败");
        }
    }

    /**
     * MFA登录验证
     * 
     * @param account 账号
     * @param code    验证码
     * @return 验证结果
     */
    @PostMapping("/mfa/login-verify")
    @ResponseBody
    public ResponseEntity<String> verifyMfaLogin(
            @RequestParam String account,
            @RequestParam String code) {
        // 验证MFA登录验证码
        boolean verified = mfaService.verifyMfaCode(account, code);

        if (verified) {
            return ResponseEntity.ok("验证成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码无效或已过期");
        }
    }

    // 辅助方法：验证是否是管理员token
    private boolean validateAdminToken(String token) {
        return jwtUtil.validateToken(token, "SysAdmin");
    }

    // 处理异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}