package cn.gyk.orderserver.mapper;

import cn.gyk.orderserver.domain.dto.AddOrderParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AddOrderListMapper extends BaseMapper<AddOrderParams> {
    void saveBatch(List<AddOrderParams> addOrderParamsList);
}
