package cn.gyk.userserver.web.filter;

import cn.gyk.userserver.domain.entity.DBUser;
import cn.gyk.userserver.web.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Collections;
import java.util.List;

/**
 * 捕获请求中的请求头，获取token字段，判断是否可以获取用户信息
 * 我们可以继承 OncePerRequestFilter 抽象类
 * <p>
 * 1、获取到用户信息之后，需要将用户的信息告知SpringSecurity，SpringSecurity会去判断你访问的接口是否有相应的权限
 * 2、告知SpringSecurity 就是使用Authentication告知框架，SpringSecurity、会将信息存储到SecurityContext中-----》SecurityContextHolder中
 * <p>
 * 登录的时候，放置的数据是用户名和密码。是要查找用的
 * 后边请求，判断权限的时候，放置进去的数据是用户的信息。密码就不需要了，还有用户的权限
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
//public class JwtAuthFilter {
    @Resource
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response,@Nonnull FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("Authorization");
        System.out.println("token: " + token);
        if (token == null) {
            doFilter(request, response,filterChain);
            return;
        }
        // 有token，通过jwt工具类，解析用户信息
        Claims claims = null;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("验签失败！！！");
            throw new RuntimeException(e);
        }
        System.out.println("claims======>" + claims);
        // 获取到了数据，将数据取出，放到UmsSysUser中
        String id = claims.get("userId", String.class);
        String username = claims.get("username", String.class);
        String userAvatar = claims.get("userAvatar", String.class);
        Integer status = claims.get("status", Integer.class);
        // 将信息放到User类中
        DBUser dbUser = new DBUser();
        dbUser.setId(id);
        dbUser.setUsername(username);
        dbUser.setUserAvatar(userAvatar);
        dbUser.setStatus(status);
        System.out.println("DbUser=====>" + dbUser);
        // TODO 将用户信息放到SecurityContext中
        // 创建一个空的权限集合
        List<GrantedAuthority> noAuthorities = Collections.emptyList();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dbUser,null,noAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
