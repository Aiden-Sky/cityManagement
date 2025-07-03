package com.example.citymanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // 使用固定的安全密钥（在实际生产环境中，应该从配置文件中读取）
    private final String SECRET_KEY = "CityManagementSecretKey_MustBe_AtLeast256bits_Long_ForSecurity";
    private final long EXPIRATION_TIME = 10 * 60 * 60 * 1000; // 10小时

    // 获取加密密钥
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 生成token
    public String generateToken(String username, String userType) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(username)
                .claim("userType", userType) // 添加用户类型声明
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // 验证token
    public boolean validateToken(String token, String expectedUserType) {
        try {
            Claims claims = extractClaims(token);
            String userType = claims.get("userType", String.class);

            // 检查用户类型是否匹配
            return expectedUserType.equals(userType);
        } catch (Exception e) {
            return false;
        }
    }

    // 从token中提取用户名
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // 从token中提取用户类型
    public String getUserTypeFromToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.get("userType", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // 提取Claims
    private Claims extractClaims(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(getSigningKey())
                .build();

        return parser.parseSignedClaims(token).getPayload();
    }
}