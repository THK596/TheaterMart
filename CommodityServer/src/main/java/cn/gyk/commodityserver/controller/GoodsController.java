package cn.gyk.commodityserver.controller;

import cn.gyk.commodityserver.domain.entity.Goods;
import cn.gyk.commodityserver.service.impl.GoodsServiceImpl;

import cn.gyk.commonserver.domain.vo.BatchDeleteVo;
import cn.gyk.commonserver.utils.R;
import cn.gyk.commonserver.utils.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Resource
    GoodsServiceImpl goodsService;

//    http://localhost:8086/api/goods/getAllGoods?current=1&size=5
    @Operation(summary = "查询商品信息")
    @GetMapping("/getAllGoods")
    public R<Page<Goods>> getAllGoods(
            @RequestParam("current")Long current,
            @RequestParam("size")Long size,
            @RequestParam(value = "category",required = false)String category,
            @RequestParam(value = "goodName",required = false)String goodName
    ) {

        return goodsService.getAllGoods(current,size,category,goodName);
    }

    @Operation(summary = "ID查询商品信息")
    @GetMapping("/getGoodsByIds")
    public R<Page<Goods>> getGoodsByIds(@RequestParam("current")Long current,
                                        @RequestParam("size")Long size,
                                        @RequestParam("ids") Collection<String> ids) {
        return goodsService.getGoodsByIds(current,size,ids);
    }


    @Operation(summary = "添加商品信息")
    @PostMapping("/addGoods")
    public Result addGoods(@RequestBody Goods goods) {
        return goodsService.addGoods(goods);
    }

    @Operation(summary = "修改商品信息")
    @PostMapping("/updateGoods")
    public Result updateGoods(@RequestBody Goods goods) {
        return goodsService.updateGoods(goods);
    }

    @Operation(summary = "批量删除商品信息")
    @DeleteMapping("/deleteGoods")
    public Result deleteGoods(@RequestBody BatchDeleteVo batchDeleteGoodsVo) {
        return goodsService.deleteGoods(batchDeleteGoodsVo.getBatchDeleteListId());
    }
}
