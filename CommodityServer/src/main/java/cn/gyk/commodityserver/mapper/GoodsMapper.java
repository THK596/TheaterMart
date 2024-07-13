package cn.gyk.commodityserver.mapper;

import cn.gyk.commodityserver.domain.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
}
