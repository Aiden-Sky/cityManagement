package com.example.citymanagement.util;

import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * TOTP工具类，用于生成和验证TOTP验证码
 */
@Component
public class TotpUtil {

    // 基于时间的步长，单位为秒，默认30秒
    private static final int TIME_STEP = 30;
    // 密钥长度
    private static final int SECRET_KEY_LENGTH = 20;
    // 验证码长度
    private static final int CODE_DIGITS = 6;
    // 算法
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    /**
     * 生成随机密钥
     *
     * @return 随机密钥
     */
    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[SECRET_KEY_LENGTH];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    /**
     * 生成TOTP二维码URL
     *
     * @param issuer    发行方（应用名称）
     * @param account   账号
     * @param secretKey 密钥
     * @return 二维码URL
     */
    public String generateQrCodeUrl(String issuer, String account, String secretKey) {
        try {
            String encodedIssuer = URLEncoder.encode(issuer, StandardCharsets.UTF_8.toString());
            String encodedAccount = URLEncoder.encode(account, StandardCharsets.UTF_8.toString());
            String encodedKey = URLEncoder.encode(secretKey, StandardCharsets.UTF_8.toString());

            return String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=%d&period=%d",
                    encodedIssuer, encodedAccount, encodedKey, encodedIssuer, CODE_DIGITS, TIME_STEP);
        } catch (Exception e) {
            throw new RuntimeException("生成二维码URL失败", e);
        }
    }

    /**
     * 验证TOTP验证码
     *
     * @param secretKey 密钥
     * @param code      待验证的验证码
     * @return 是否有效
     */
    public boolean verifyCode(String secretKey, String code) {
        if (code == null || code.length() != CODE_DIGITS) {
            return false;
        }

        try {
            Base32 base32 = new Base32();
            byte[] decodedKey = base32.decode(secretKey);

            // 获取当前时间戳
            long currentTimeMillis = System.currentTimeMillis();
            // 计算当前时间步
            long currentStep = currentTimeMillis / 1000 / TIME_STEP;

            // 验证当前时间步的验证码
            String currentCode = generateCodeForStep(decodedKey, currentStep);
            if (code.equals(currentCode)) {
                return true;
            }

            // 验证前一个时间步的验证码（允许30秒的时间误差）
            String previousCode = generateCodeForStep(decodedKey, currentStep - 1);
            return code.equals(previousCode);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 为指定的时间步生成验证码
     *
     * @param key  密钥
     * @param step 时间步
     * @return 验证码
     */
    private String generateCodeForStep(byte[] key, long step) throws Exception {
        byte[] timeBytes = new byte[8];
        long value = step;

        // 将时间步转为字节数组
        for (int i = 7; i >= 0; i--) {
            timeBytes[i] = (byte) (value & 0xff);
            value >>= 8;
        }

        // 使用HMAC-SHA1算法生成哈希
        SecretKeySpec signKey = new SecretKeySpec(key, HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signKey);
        byte[] hash = mac.doFinal(timeBytes);

        // 动态截断
        int offset = hash[hash.length - 1] & 0x0f;
        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8)
                | (hash[offset + 3] & 0xff);

        // 取模，得到指定长度的验证码
        int otp = binary % (int) Math.pow(10, CODE_DIGITS);

        // 格式化为固定长度的字符串
        return String.format("%0" + CODE_DIGITS + "d", otp);
    }
}