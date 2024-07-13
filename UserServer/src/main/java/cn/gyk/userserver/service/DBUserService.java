package cn.gyk.userserver.service;

import cn.gyk.commonserver.utils.LoginR;
import cn.gyk.userserver.domain.dto.LoginParams;
import cn.gyk.userserver.domain.entity.DBUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DBUserService extends IService<DBUser> {
    LoginR UserLogin(LoginParams loginParams);
}
