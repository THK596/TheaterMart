package cn.gyk.cartserver.controller;


import cn.gyk.cartserver.domain.entity.Cart;
import cn.gyk.cartserver.service.impl.CartServiceImpl;
import cn.gyk.commonserver.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "购物车管理")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartServiceImpl cartServiceImpl;

    @Operation(summary = "添加购物车")
    @PostMapping("/genCart")
    public Result genCart(Cart cart) {
        return cartServiceImpl.genCart(cart);
    }
}
