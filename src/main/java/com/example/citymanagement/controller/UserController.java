package com.example.citymanagement.controller;

import com.example.citymanagement.service.UserService;
import com.example.citymanagement.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> login(@RequestParam String account, @RequestParam String password) {
        // 处理登录逻辑
        // 例如：验证用户名和密码，生成JWT token等
        String token = userService.authenticate(account, password);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("账户或密码不正确");
        }
    }

    @PostMapping("/verifyToken")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> verifyToken(@RequestBody String body) {
        //提取token
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            String token = rootNode.path("token").asText();

            boolean tokenVarited = false;
            tokenVarited = jwtUtil.validateToken(token);
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
}
