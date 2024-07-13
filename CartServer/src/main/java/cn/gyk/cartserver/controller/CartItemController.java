package cn.gyk.cartserver.controller;


import cn.gyk.cartserver.domain.dto.CartGoodsDto;
import cn.gyk.cartserver.domain.entity.CartItem;
import cn.gyk.cartserver.domain.dto.UpdateCartParams;
import cn.gyk.cartserver.service.impl.CartItemServiceImpl;
import cn.gyk.commonserver.domain.vo.BatchDeleteVo;
import cn.gyk.commonserver.utils.R;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.tmapi.dto.GoodsDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@Tag(name = "购物车项管理")
@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {

    @Resource
    private CartItemServiceImpl cartItemServiceImpl;

    @Operation(summary = "根据购物车项查询商品信息")
    @GetMapping("/getGoodsByIds")
    public R<Page<CartGoodsDto>> getCartItemListByIds(
            @RequestParam("current")long current,
            @RequestParam("size")long size){
        return cartItemServiceImpl.getCartItemsByIds(current, size);
    }

    @Operation(summary = "根据用户ID添加购物车商品")
    @PostMapping("/addCartItem")
    public Result addCartItems(@RequestBody UpdateCartParams updateCartParams) {
        return cartItemServiceImpl.addCartItems(updateCartParams);
    }

    @Operation(summary = "批量删除购物车商品")
    @DeleteMapping("/deleteCartItem")
    @ResponseBody
    public Result deleteCartItems(@RequestBody BatchDeleteVo batchDeleteCartItemVo) {
        return cartItemServiceImpl.deleteCartItems(batchDeleteCartItemVo.getBatchDeleteListId());
    }

    @Operation(summary = "更新购物车信息")
    @PostMapping("/updateCartItem")
    public Result updateCartItems(@RequestBody CartItem cartItem) {
        return cartItemServiceImpl.updateCartItems(cartItem);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/deleteOneCart/{id}")
    public Result deleteOneCartItem(@PathVariable(value = "id") String id) {
        return cartItemServiceImpl.deleteOneCartItem(id);
    }

}
