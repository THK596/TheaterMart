package cn.gyk.tmapi.client;

import cn.gyk.tmapi.dto.GoodsDto;
import cn.gyk.commonserver.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient("CommodityServer")
public interface GoodsClient {

    @GetMapping("api/goods/getGoodsByIds")
    R<Page<GoodsDto>> getGoodsByIds( @RequestParam("current")long current,
                                     @RequestParam("size")long size,
                                     @RequestParam("ids") Collection<String> ids);
}
