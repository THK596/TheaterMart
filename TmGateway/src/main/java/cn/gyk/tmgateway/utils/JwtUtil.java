package cn.gyk.tmgateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;


@Component
public class JwtUtil {


    private static final String SECRET = "S/4AN9IsSRUC~{0c4]y#$F2XbV8^`#a14vawn<~Kr@(D%3TF-p1s/h{Y9k7y((rR";
    private static final long defaultExpire = 1000 * 60 * 60 * 24 * 7L;//7天
    // 创建一个jwt密钥 加密和解密都需要用这个玩意
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成token
     * @param claims 请求体数据
     * @return token
     */
    public String generateToken(Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + defaultExpire);
        // 生成token
        return builder
//                .issuer("8087") // 签发这
                .claims(claims) // 数据
//                .subject(username) //主题
                .issuedAt(now) // 签发时间
                .expiration(expiryDate) // 过期时间
                .signWith(key) // 签发方式
                .compact();// 压缩
    }

    public Claims parseToken(String token) {
//        if(token == null || token.isEmpty()) {
//            throw new JwtException("Token is empty");
//        }
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
