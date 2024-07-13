package cn.gyk.orderserver.controller;


import cn.gyk.commonserver.utils.R;
import cn.gyk.orderserver.domain.entity.Order;
import cn.gyk.orderserver.service.impl.OrderInfoServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单详细管理")
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderInfoServiceImpl orderInfoService;

    @Operation(summary = "查询用户订单信息")
    @GetMapping("/getOrderInfo")
    public R<Page<Order>> getOrderList(@RequestParam("current")long current,
                                        @RequestParam("size")long size) {
        return orderInfoService.getAllOrderList(current,size);
    }
}
