package com.example.citymanagement.controller;

import com.example.citymanagement.entity.Resident;
import com.example.citymanagement.entity.User;
import com.example.citymanagement.service.ResidentService;
import com.example.citymanagement.service.UserService;
import com.example.citymanagement.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import javax.imageio.ImageIO;
import org.springframework.web.bind.annotation.*;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 上传用户信息
    @PostMapping("/setUserInfo")
    @ResponseBody
    public ResponseEntity<String> addUserInfo(@RequestBody User user) {
        if (userService.setUser(user)) {
            return ResponseEntity.ok("用户信息设置成功");
        } else {
            return ResponseEntity.status(500).body("用户信息设置失败");
        }
    }

    // 处理登录
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Object> login(@RequestParam String account, @RequestParam String password) {
        // 处理登录逻辑
        // 例如：验证用户名和密码，生成JWT token等
        String token = userService.authenticate(account, password);
        if (token != null) {
            // 检查用户是否是管理人员
            User user = userService.getUserByAccount(account);
            if (user != null) {

                return ResponseEntity.ok(token);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取用户信息失败");
        } else {
            return ResponseEntity.status(401).body("账户或密码不正确");
        }
    }



    // 检查账号是否可用
    @GetMapping("/check-account")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkAccountAvailable(@RequestParam String account) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.getUserByAccount(account);
        if (user != null) {
            response.put("available", false);
            response.put("message", "该账号已被注册");
        } else {
            response.put("available", true);
            response.put("message", "账号可用");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyToken")
    @ResponseBody
    public ResponseEntity<String> verifyToken(@RequestBody String body) {
        // 提取token
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            String token = rootNode.path("token").asText();
            String type = rootNode.path("type").asText();

            boolean tokenValid = false;
            // 使用新的JwtUtil验证方法，不再需要传入不同的密钥
            tokenValid = jwtUtil.validateToken(token, type);

            if (tokenValid) {
                return ResponseEntity.ok("验证成功");
            } else {
                return ResponseEntity.status(401).body("无效token，已过期或失效");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("token验证过程中发生错误");
        }
    }

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/getcapchar")
    public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码文本并保存到 session 中
        String captchaText = defaultKaptcha.createText();
        request.getSession().setAttribute("captcha", captchaText);

        // 生成验证码图片并写入响应
        BufferedImage captchaImage = defaultKaptcha.createImage(captchaText);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "jpg", outputStream);
        byte[] captchaImageBytes = outputStream.toByteArray();

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        response.getOutputStream().write(captchaImageBytes);
    }

    @PostMapping("/verifycapcha")
    @ResponseBody
    public ResponseEntity<String> verifyCaptcha(@RequestParam("captcha") String captcha, HttpServletRequest request) {
        String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
        if (sessionCaptcha != null && sessionCaptcha.equalsIgnoreCase(captcha)) {
            return ResponseEntity.ok("验证码正确");
        } else {
            return ResponseEntity.status(400).body("验证码错误");
        }
    }
}
