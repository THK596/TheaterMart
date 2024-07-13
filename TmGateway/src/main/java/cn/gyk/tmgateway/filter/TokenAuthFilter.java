package cn.gyk.tmgateway.filter;



import cn.gyk.tmgateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class TokenAuthFilter implements GlobalFilter {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisTemplate<String, Object> redisTemplate; // Note the generics

//    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1. 获取request
        ServerHttpRequest request = exchange.getRequest();
//        2.判断是否需要做登录拦截
        if(isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
//        3.获取token
        String headers = request.getHeaders().getFirst("Authorization");
        assert headers != null;
        if(!headers.isEmpty()) {
            System.out.println("token===============> " + headers);
        }
//        4.校验并解析token
        Claims claims;
        try {
            claims = jwtUtil.parseToken(headers);
            System.out.println("claims===========>"+claims);
        } catch (Exception e) {
            throw new RuntimeException("token解析失败");
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
        }
        //TODO 5传递用户信息
        System.out.println("claims========>"+claims);

        // 6.放行
        return chain.filter(exchange);
    }

    private boolean isExclude(String path) {
        //放行登录路径
        return path.equals("/api/auth/toLogin");
    }

}
