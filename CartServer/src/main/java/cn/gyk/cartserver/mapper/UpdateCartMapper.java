package cn.gyk.cartserver.mapper;

import cn.gyk.cartserver.domain.dto.UpdateCartParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UpdateCartMapper extends BaseMapper<UpdateCartParams> {

}
