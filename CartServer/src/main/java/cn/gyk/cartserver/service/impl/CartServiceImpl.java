package cn.gyk.cartserver.service.impl;

import cn.gyk.cartserver.domain.entity.Cart;
import cn.gyk.cartserver.mapper.CartMapper;
import cn.gyk.cartserver.utils.JwtUtil;
import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.Result;
import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Service
public class CartServiceImpl {


    @Resource
    private CartMapper cartMapper;
    @Resource
    private JwtUtil jwtUtil;


    /**
     * 生成用户购物车给
     * @param cart 购物车实体类
     * @return Result
     */
    public Result genCart(Cart cart) {
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 从请求头获取Authorization
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 解析Token获取用户ID
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
            System.out.println("Header解析Token=======>"+ claims);
        } catch (Exception e) {
            throw new RuntimeException("Token解析失败");
        }
        // 获取用户ID
        String userId1 = claims.get("subject", String.class);
        System.out.println("获取Token用户ID"+userId1);
        // 设置购物车用户ID
        cart.setId(IdUtil.getSnowflakeNextIdStr());
        Date Date = new Date();
        cart.setCreateTime(Date);
        cart.setUpdateTime(Date);
        cart.setUserId(userId1);
        int i = cartMapper.insert(cart);
        if (i>0) {
            return Result.success(cart);
        } else {
            return Result.failed(Constants.CODE_401,null);
        }
    }
}
