package com.example.citymanagement.controller;

import com.example.citymanagement.service.UserService;
import com.example.citymanagement.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 上传用户信息
    @GetMapping("/AddUserInfo")
    @ResponseBody
    public boolean addUserInfo(@RequestParam("userID") String account,
                               @RequestParam("userName") String userName,
                               @RequestParam("userAge") int userAge) {
        boolean res = userService.addUserInfo(account, userName, userAge);
        return res;
    }

    // 处理登录
    @PostMapping("/login")
    @ResponseBody

    public ResponseEntity<Object> login(@RequestParam String account, @RequestParam String password) {
        // 处理登录逻辑
        // 例如：验证用户名和密码，生成JWT token等
        String token = userService.authenticate(account, password);
        if (token != null) {
            if (userService.checkManage(account)){
                 Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("isManage", true);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("账户或密码不正确");
        }
    }

    @PostMapping("/verifyToken")
    @ResponseBody
    public ResponseEntity<String> verifyToken(@RequestBody String body) {
        //提取token
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            String token = rootNode.path("token").asText();
            String type = rootNode.path("type").asText();

            boolean tokenVarited = false;
            switch (type) {
                case "user":
                    tokenVarited = jwtUtil.validateToken(token, "Resident");
                    break;
                case "admin":
                    tokenVarited = jwtUtil.validateToken(token, "SystemAdmin");
                    break;
                case "manage":
                    tokenVarited = jwtUtil.validateToken(token, "Management");
                    break;
                default:
                    tokenVarited = false;
            }


            if (tokenVarited != false) {
                return ResponseEntity.ok("验证成功");
            } else {
                return ResponseEntity.status(401).body("无效token，已过期或失效");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public String verifyCaptcha(@RequestParam("captcha") String captcha, HttpServletRequest request) {
        String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
        if (sessionCaptcha != null && sessionCaptcha.equalsIgnoreCase(captcha)) {
            return "验证码正确";
        } else {
            return "验证码错误";
        }
    }

}
