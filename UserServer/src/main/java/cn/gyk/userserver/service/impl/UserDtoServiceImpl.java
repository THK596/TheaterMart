package cn.gyk.userserver.service.impl;

import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.tmapi.dto.UserDto;
import cn.gyk.userserver.mapper.UserDtoMapper;
import cn.gyk.userserver.service.UserDtoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserDtoServiceImpl extends ServiceImpl<UserDtoMapper,UserDto> implements UserDtoService {
    /**
     * 根据用户ID获取用户信息（API接口）
     * @param id 用户ID
     * @return Result
     */
    public Result getUserById(String id) {
        UserDto userDto = baseMapper.selectById(id);
        if(userDto == null) {
            return Result.failed(Constants.CODE_401,null);
        } else {
            return Result.success(userDto);
        }
    }
}
