package com.example.citymanagement.controller;

import com.example.citymanagement.Dto.ResidentRegisterDTO;
import com.example.citymanagement.entity.Resident;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.service.ResidentService;
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
@RequestMapping("/resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 管理员获取所有居民信息
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Resident>> getAllResidents(@RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(residentService.getAllResidents());
    }

    // 获取当前登录居民自己的信息
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<Resident> getResidentInfo(@RequestHeader("Authorization") String token) {
        try {
            // 验证居民权限
            if (!jwtUtil.validateToken(token, "Resident")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // 从token中获取用户名
            String username = jwtUtil.getUsernameFromToken(token);
            Resident resident = residentService.getResidentByAccount(username);

            if (resident != null) {
                // 出于安全考虑，清除密码信息
                resident.setPasswordHash(null);
                return ResponseEntity.ok(resident);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 管理员获取特定居民信息
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Resident> getResidentById(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Resident resident = residentService.getResidentById(id);
        if (resident != null) {
            // 出于安全考虑，清除密码信息
            resident.setPasswordHash(null);
            return ResponseEntity.ok(resident);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 管理员添加新居民
//    @PostMapping("/add")
//    @ResponseBody
//    public ResponseEntity<String> addResident(@RequestBody Resident resident,
//            @RequestHeader("Authorization") String token) {
//        // 验证管理员权限
//        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
//        }
//
//        try {
//            if (residentService.registResident(resident)) {
//                return ResponseEntity.ok("居民添加成功");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("居民添加失败");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("居民添加失败：" + e.getMessage());
//        }
//    }

    // 更新居民信息
    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateResident(@RequestBody Resident resident,
            @RequestHeader("Authorization") String token) {
        try {
            // 获取token中的用户名
            String username = jwtUtil.getUsernameFromToken(token);

            // 验证是否是管理员或者是居民本人
            boolean isAdmin = jwtUtil.validateToken(token, "SystemAdmin");
            boolean isSelf = jwtUtil.validateToken(token, "Resident") && username.equals(resident.getAccount());

            if (!isAdmin && !isSelf) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
            }

            if (residentService.updateResident(resident)) {
                return ResponseEntity.ok("居民信息更新成功");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("居民信息更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("居民信息更新失败：" + e.getMessage());
        }
    }

    // 管理员删除居民
    @DeleteMapping("/delete/{account}")
    @ResponseBody
    public ResponseEntity<String> deleteResident(@PathVariable String account,
            @RequestHeader("Authorization") String token) {
        // 验证管理员权限
        if (!jwtUtil.validateToken(token, "SystemAdmin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无权限执行此操作");
        }

        if (residentService.deleteResident(account)) {
            return ResponseEntity.ok("居民删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("居民删除失败");
        }
    }

    // 居民注册
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Object> registerResident(@RequestBody ResidentRegisterDTO residentRegisterDTO) {
        try {
            // 检查账号是否已存在
            User existingUser = userService.getUserByAccount(residentRegisterDTO.getAccount());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("账号已存在");
            }

            // 添加居民信息
            boolean success = residentService.registResident(residentRegisterDTO);

            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "注册成功");
                response.put("account", residentRegisterDTO.getAccount());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("注册失败，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("注册失败：" + e.getMessage());
        }
    }

}