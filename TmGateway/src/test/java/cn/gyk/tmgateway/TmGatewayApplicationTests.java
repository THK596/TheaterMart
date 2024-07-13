package cn.gyk.tmgateway;


import cn.gyk.tmgateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;

@SpringBootTest
class TmGatewayApplicationTests {
    @Resource
    private JwtUtil jwtUtil;



    @Test
    public void test() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id","84df8029-32a3-49e3-a192-189b6c12712b");
        map.put("username","admin");
        map.put("userAvatar", "NULL");
        map.put("status","1");
        String token = jwtUtil.generateToken(map);
        System.out.println("token====>" + token);
    }

    @Test
    public void test2() {
        Claims claims = jwtUtil.parseToken("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQXZhdGFyIjoiTlVMTCIsImlkIjoiODRkZjgwMjktMzJhMy00OWUzLWExOTItMTg5YjZjMTI3MTJiIiwidXNlcm5hbWUiOiJhZG1pbiIsInN0YXR1cyI6MSwiaWF0IjoxNzE2MDg3MzQ2LCJleHAiOjE3MTY2OTIxNDZ9.OjzFhCbzR_w6q-zYeJgdzOYIPkuZfljZpNLbkUHr750");
        System.out.println("claims====>" + claims);
    }
}
