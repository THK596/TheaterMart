package cn.gyk.cartserver.mapper;

import cn.gyk.cartserver.domain.entity.CartItem;
import cn.gyk.cartserver.domain.dto.UpdateCartParams;
import cn.gyk.commonserver.domain.dto.DeleteParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartItemMapper extends BaseMapper<CartItem> {

      int insertByUserId(UpdateCartParams updateCartParams);

      UpdateCartParams selectByUserIdAndProductId(@Param("userId") String userId,@Param("productId") String productId);

      int updateById1(UpdateCartParams existingCartItem);

    int deleteByUserAndGoodIds(@Param("userId") String userId, @Param("goodIdList") List<String> goodIdList);
}
