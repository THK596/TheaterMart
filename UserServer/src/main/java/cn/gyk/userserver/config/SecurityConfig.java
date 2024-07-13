package cn.gyk.userserver.config;


import cn.gyk.userserver.web.MyUserDetailService;
import cn.gyk.userserver.web.filter.JwtAuthFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;


@Configuration
public class SecurityConfig {

    @Resource
    private JwtAuthFilter jwtAuthFilter;

    @Resource
    private MyUserDetailService myUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 构建密码编辑器
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager：负责认证的
     * DaoAuthenticationProvider：负责将 sysUserDetailsService、passwordEncoder融合起来送到AuthenticationManager中
     * @param passwordEncoder 密码编译器
     * @return ProviderManager()
     */

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // 关联使用的密码编码器
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        // 将daoAuthenticationProvider放置进 AuthenticationManager 中,包含进去
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        // 创建一个包含 daoAuthenticationProvider 的 ProviderManager
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider));
    }

    /**
     * 配置过滤器链，对login接口放行
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 临时禁用CSRF测试
        http.csrf(AbstractHttpConfigurer::disable);
        // 放行所有访问到登陆页面
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/toLogin").permitAll().anyRequest().authenticated());

        // TODO 将过滤器添加到过滤器链中
        // 将过滤器添加到 UsernamePasswordAuthenticationFilter 之前
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
