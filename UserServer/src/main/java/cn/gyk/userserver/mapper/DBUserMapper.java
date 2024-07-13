package cn.gyk.userserver.mapper;


import cn.gyk.userserver.domain.dto.PasswordChangeParameters;
import cn.gyk.userserver.domain.entity.DBUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DBUserMapper extends BaseMapper<DBUser> {
//    int updateById(PasswordChangeParameters passwordChangeParameters);
}
