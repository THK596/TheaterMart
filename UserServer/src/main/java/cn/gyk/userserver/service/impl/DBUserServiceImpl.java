package cn.gyk.userserver.service.impl;

import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.LoginR;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.userserver.domain.dto.LoginParams;
import cn.gyk.userserver.domain.dto.PasswordChangeParameters;
import cn.gyk.userserver.domain.entity.DBUser;
import cn.gyk.userserver.mapper.DBUserMapper;
import cn.gyk.userserver.service.DBUserService;
import cn.gyk.userserver.web.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class DBUserServiceImpl extends ServiceImpl<DBUserMapper, DBUser> implements DBUserService {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private RedisTemplate<String, Object> redisTemplate; // Note the generics


    @Resource
    private AmqpTemplate amqpTemplate;

    @Resource
    DBUserMapper dbUserMapper;

    @Resource
    private JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public DBUserServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /*
    用户信息存储在SecurityContext的Authentication中
    Principal => 用户信息
    Credentials => 用户密码
    Authorities => 用户权限
     */

    /**
     * 用户登录(自定义SpringSecurity)
     * @param loginParams 登录参数
     * @return LoginR
     */
    @Override
    public LoginR UserLogin(LoginParams loginParams) {
        String username = loginParams.getUsername();
        // 传入用户名和密码 将是否认证标记设置为false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginParams.getUsername(),
                        loginParams.getPassword());
        // 实现登录逻辑，此时就会去调用 loadUserByUsername方法
        // 返回的 Authentication 其实就是 UserDetails
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            log.error("登陆异常");
            // 记录登录失败次数
            Integer count = (Integer) redisTemplate.opsForValue().get(username + "_login_error_count");
            if (count == null) {
                count = 0;
            }
            count++;
            redisTemplate.opsForValue().set(username + "_login_error_count", count);

            //  如果错误次数达到3次，锁定账户
            if (count >= 3) {
                // 使用 QueryWrapper 修改数据库用户状态为锁定状态
                LambdaQueryWrapper<DBUser> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DBUser::getUsername, username);
                DBUser dbUser = new DBUser();
                dbUser.setStatus(0);
                dbUserMapper.update(dbUser, queryWrapper);
                return LoginR.error(Constants.CODE_400,"账户已被锁定，请联系管理员");
            }
            return LoginR.error(Constants.CODE_400,"账号或密码错误！！");
        }
        // 获取返回的用户
        DBUser dbUser = (DBUser) authentication.getPrincipal();
        if (dbUser == null) {
            return LoginR.error(Constants.CODE_400,"账号或密码错误！！");
        }
        log.info("登陆后的用户==========》{}",dbUser);
        // 将用户信息通过JWT生成token，返回给前端
        HashMap<String,Object> map = new HashMap<>();
        // 在 JWT 规范中，subject 属性通常用于存储用户ID
        map.put("subject",dbUser.getId());
        map.put("username",dbUser.getUsername());
        map.put("userAvatar",dbUser.getUserAvatar());
        map.put("status",dbUser.getStatus());
        String token = jwtUtil.generateToken(map);
        // 登录成功，清除错误次数
        redisTemplate.delete(dbUser.getUsername() + "_login_error_count");
        return LoginR.ok(token);

    }



    /**
     * 修改用户密码
     * @param passwordChangeParameters 修改密码参数
     */
    public void changePassword(PasswordChangeParameters passwordChangeParameters) {
        try {
            DBUser dbUser = baseMapper.selectById(passwordChangeParameters.getId());
            //使用 BCryptPasswordEncoder 验证密码
            if(!bCryptPasswordEncoder.matches(passwordChangeParameters.getCurrentPassword(), dbUser.getPassword())) {
                throw new Exception("信息错误");
            }
            // 判断新密码与旧密码一致
            if(passwordChangeParameters.getNewPassword().equals(passwordChangeParameters.getCurrentPassword())){
                throw new RuntimeException("新密码不能与旧密码一致");
            }
            //修改密码，使用 BCryptPasswordEncoder 加密新密码
            dbUser.setPassword(bCryptPasswordEncoder.encode(passwordChangeParameters.getNewPassword()));
            baseMapper.updateById(dbUser);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }
}
