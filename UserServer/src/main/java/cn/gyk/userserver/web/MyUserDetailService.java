package cn.gyk.userserver.web;


import cn.gyk.userserver.domain.dto.SysUser;
import cn.gyk.userserver.domain.entity.DBUser;
import cn.gyk.userserver.mapper.DBUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private DBUserMapper DBUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<DBUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DBUser::getUsername, username);
        DBUser dbUser = DBUserMapper.selectOne(queryWrapper);
        // 查询失败抛出异常
        if (Objects.isNull(dbUser)) {
            throw new UsernameNotFoundException(username+"用户名或者密码错误");
        }
        // TODO 查询对应的权限信息

        //封装成UserDetails返回
        return new SysUser(dbUser);
    }
}
