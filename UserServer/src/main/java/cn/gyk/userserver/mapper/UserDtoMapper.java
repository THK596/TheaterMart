package cn.gyk.userserver.mapper;

import cn.gyk.tmapi.dto.UserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDtoMapper extends BaseMapper<UserDto> {
}
