package cn.gyk.orderserver.controller;

import cn.gyk.commonserver.utils.R;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.orderserver.domain.dto.AddOrderParams;
import cn.gyk.orderserver.domain.dto.OrderGoodsDto;
import cn.gyk.orderserver.service.impl.OrderInfoServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单详细管理")
@RestController
@RequestMapping("/api/order_details")
public class OrderDetailController {
    @Resource
    private OrderInfoServiceImpl orderServiceImpl;

    @Operation(summary = "分页查询订单详细信息")
    @GetMapping("/getOrderList")
    public R<Page<OrderGoodsDto>> getOrderDetailPage(@RequestParam("current")long current,
                                                     @RequestParam("size")long size) {
        return orderServiceImpl.getOrderList(current, size);
    }

    @Operation(summary = "添加订单信息")
    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody List<AddOrderParams> addOrderParamsList) {
        return orderServiceImpl.addOrder(addOrderParamsList);
    }
}
