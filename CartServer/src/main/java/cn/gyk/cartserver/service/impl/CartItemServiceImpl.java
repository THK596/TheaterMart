package cn.gyk.cartserver.service.impl;

import cn.gyk.cartserver.domain.dto.CartGoodsDto;
import cn.gyk.cartserver.domain.dto.CartItemDetails;
import cn.gyk.cartserver.domain.entity.CartItem;

import cn.gyk.cartserver.domain.dto.UpdateCartParams;
import cn.gyk.cartserver.mapper.CartItemDetailsMapper;
import cn.gyk.cartserver.mapper.CartItemMapper;
import cn.gyk.cartserver.mapper.UpdateCartMapper;
import cn.gyk.cartserver.utils.JwtUtil;
import cn.gyk.commonserver.domain.dto.DeleteParams;
import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.R;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.tmapi.client.GoodsClient;
import cn.gyk.tmapi.dto.GoodsDto;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class CartItemServiceImpl {
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private CartItemDetailsMapper cartItemDetailsMapper;
    @Resource
    private CartItemMapper cartItemMapper;
    @Resource
    private UpdateCartMapper updateCartMapper;


    //获取Headers的用户信息
    public String getUserInfo() {
        // 请求当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 从请求头获取AUTHORIZATION
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 解析token获取用户ID
        Claims claims;
        try {
            claims = jwtUtil.parseToken(token);
        } catch (Exception e) {
            throw new RuntimeException("TOKEN解析失败");
        }
        // 获取用户ID
        return claims.get("subject", String.class);
    }

    /**
     * 分页查询登录用户的购物车商品
     * @param current 当前页数
     * @param size 展示数据数量
     * @return R
     */
    public R<Page<CartGoodsDto>> getCartItemsByIds(long current, long size) {
        String userId = getUserInfo();

        // 获取购物车项列表
        List<CartItemDetails> cartItemDetailsList = cartItemDetailsMapper.GetUserCartList(userId);

        // 提取product_id并添加到ids集合中
        Set<String> ids = new HashSet<>();
        for (CartItemDetails cartItemDetails : cartItemDetailsList) {
            ids.add(cartItemDetails.getProductId());
        }
        // 如果ids为空，则可能不需要继续查询商品信息
        if(ids.isEmpty()) {
            // 返回空列表或者包含特定错误信息的响应
            return R.error("购物车中没有商品");
        }
        // 获取商品信息
        R<Page<GoodsDto>> goodsResponse = goodsClient.getGoodsByIds(current, size, ids);
        Page<GoodsDto> goodsPage = goodsResponse.getData();
        List<GoodsDto> goodsList = goodsPage.getRecords();

        // 将购物车详细信息和商品详细信息结合
        List<CartGoodsDto> cartGoodsDtoList = new ArrayList<>();
        for (CartItemDetails cartItemDetails : cartItemDetailsList) {
            for(GoodsDto goodsDto : goodsList) {
                if(cartItemDetails.getProductId().equals(goodsDto.getId())) {
                    CartGoodsDto cartGoodsDto = new CartGoodsDto();
                    cartGoodsDto.setId(cartItemDetails.getId());
                    cartGoodsDto.setUserId(cartItemDetails.getUserId());
                    cartGoodsDto.setProductId(cartItemDetails.getProductId());
                    cartGoodsDto.setQuantity(String.valueOf(cartItemDetails.getQuantity()));
                    cartGoodsDto.setGoodName(goodsDto.getGoodName());
                    cartGoodsDto.setPrice(goodsDto.getPrice());
                    cartGoodsDtoList.add(cartGoodsDto);
                    break;
                }
            }
        }
        // 创建分页对象
        Page<CartGoodsDto> cartGoodsDtoPage = new Page<>(current,size,cartItemDetailsList.size());
        cartGoodsDtoPage.setRecords(cartGoodsDtoList);
        return R.success("success",cartGoodsDtoPage);
    }

    /**
     * 根据登录用户ID添加购物车商品
     * @param updateCartParams 更新购物车商品参数
     * @return Result
     */
    public Result addCartItems(UpdateCartParams updateCartParams) {
        Date date = new Date();
        // 获取登录用户ID
        String userId = getUserInfo();
        // 设置登录用户ID
        updateCartParams.setUserId(userId);
        // 查询数据库中是否已存在相同用户ID和商品ID的记录
        UpdateCartParams existingCartItem = cartItemMapper.selectByUserIdAndProductId(userId, updateCartParams.getProductId());
        if(existingCartItem != null) {
            // 购物车商品数量 + 新增加相同商品数量
            existingCartItem.setQuantity(existingCartItem.getQuantity() + updateCartParams.getQuantity());
            existingCartItem.setUpdateTime(date);
            existingCartItem.setId(existingCartItem.getId());
            int updateResult = cartItemMapper.updateById1(existingCartItem);
            if(updateResult > 0) {
                return Result.success(null);
            } else {
                return Result.failed(Constants.CODE_500,null);
            }
        } else {
            // 如果不存在，则插入新记录
            // 生成ID
            updateCartParams.setId(IdUtil.getSnowflakeNextIdStr());
            //设置时间
            updateCartParams.setCreateTime(date);
            updateCartParams.setUpdateTime(date);
            int i = cartItemMapper.insertByUserId(updateCartParams);
            if(i > 0) {
                return Result.success(null);
            } else {
                return Result.failed(Constants.CODE_500,null);
            }
        }
    }

    /**
     * 更新购物车商品
     * @param cartItem 购物车项目
     * @return Result
     */
    public Result updateCartItems(CartItem cartItem) {
        int i = cartItemMapper.updateById(cartItem);
        cartItem.setUpdateTime(new Date());
        if(i > 0) {
            return Result.success(null);
        } else {
            return Result.failed(Constants.CODE_500,null);
        }
    }

    /**
     * 批量删除购物车商品
     * @param id ids列表
     * @return Result
     */
    public Result deleteCartItems(List<DeleteParams> id) {
        int i = cartItemMapper.deleteBatchIds(id);
        if(i > 0) {
            return Result.success(null);
        } else {
            return Result.failed(Constants.CODE_500,null);
        }
    }

    public Result deleteOneCartItem(String id) {
        int i = cartItemMapper.deleteById(id);
        if(i > 0) {
            return Result.success(null);
        } else {
            return Result.failed(Constants.CODE_500,null);
        }
    }
}
