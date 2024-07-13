package cn.gyk.cartserver.mapper;


import cn.gyk.cartserver.domain.dto.CartItemDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartItemDetailsMapper extends BaseMapper<CartItemDetails> {
    List<CartItemDetails> GetUserCartList(@Param("userId") String userId);
}
