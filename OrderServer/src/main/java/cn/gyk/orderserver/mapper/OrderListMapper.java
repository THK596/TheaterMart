package cn.gyk.orderserver.mapper;

import cn.gyk.orderserver.domain.vo.OrderDetailsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderListMapper extends BaseMapper<OrderDetailsVo> {
    List<OrderDetailsVo> GetOrderList(@Param("userId") String userId);
}
