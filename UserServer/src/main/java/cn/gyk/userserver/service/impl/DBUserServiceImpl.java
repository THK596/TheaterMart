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
            return LoginR.error(Constants.CODE_400,"登陆异常！请检查");
        }
        // 获取返回的用户
        DBUser dbUser = (DBUser) authentication.getPrincipal();
        if (dbUser == null) {
            return LoginR.error(Constants.CODE_400,"用户名或密码错误");
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
        redisTemplate.opsForValue().set(dbUser.getUsername(), token);



        /*
         * 测试Token解析
         */
//        try {
////            Claims claims = jwtUtil.parseToken(token);
////            redisTemplate.opsForValue().set(dbUser.getUsername(), claims);
//        } catch (Exception e) {
//            System.out.println("解析错误");
//            throw new RuntimeException(e);
//        }
        return LoginR.ok(token);

    }





//    public Result selectOneUserInfo(String email) {
//        QueryWrapper<DBUser> queryWrapper;
//        try {
//            queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("email", email);
//        } catch (Exception e) {
//            throw new RuntimeException("邮箱信息有误");
//        }
////        redisTemplate.opsForValue().set(email,email);
//        return Result.success(queryWrapper);
//    }

//    // TODO 通过消息队列发送userID
//    public Result sendCodeMessage(String email) {
//        // 获取用户信息
//        Result result = selectOneUserInfo(email);
//        if(result.getCode() == Constants.CODE_200){
//            System.out.println(result);
//            amqpTemplate.convertAndSend(UpdateUserInfo.EXCHANGE_NAME,UpdateUserInfo.ROUTING_KEY);
////            QueryWrapper<DBUser> queryWrapper = (QueryWrapper<DBUser>) result.getData();
////            DBUser dbUser = baseMapper.selectOne(queryWrapper);
//                return Result.success(null);
//        } else {
//            return Result.failed(Constants.CODE_401, result.getMsg());
//        }
//    }



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
