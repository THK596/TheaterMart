package cn.gyk.orderserver.mapper;

import cn.gyk.orderserver.domain.entity.OrderDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {

}
