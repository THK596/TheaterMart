package cn.gyk.commodityserver.service.impl;

import cn.gyk.commodityserver.domain.entity.Goods;
import cn.gyk.commodityserver.mapper.GoodsMapper;
import cn.gyk.commonserver.domain.dto.DeleteParams;
import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.R;
import cn.gyk.commonserver.utils.Result;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl {
    @Resource
    private GoodsMapper goodsMapper;

    /**
     * 分页查询查询商品数据
     * @param current 第几页
     * @param size 一页显示多少条数据
     * @param category 商品分类
     * @return R
     */
    public R<Page<Goods>> getAllGoods(long current, long size, String category, String goodName) {
        // 创建分页对象
        Page<Goods> goodsPage = new Page<>(current,size);

        // 创建 QueryWrapper 对象并添加查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();

        // 当category不为空，添加分类查询条件
        if(StringUtils.isNotBlank(category)){
            queryWrapper.like("category",category);
        }
        // 当goodsName不为空，添加模糊查询条件
        if (StringUtils.isNotBlank(goodName)) {
            queryWrapper.like("good_name", goodName);
        }
        // 执行分页查询
        goodsMapper.selectPage(goodsPage,queryWrapper);

        return R.success("success",goodsPage);
    }


    /**
     * 用ID分页查询商品信息  --API
     * @param current 第几页
     * @param size 一页显示多少条数据
     * @param ids 商品ID==>List
     * @return R
     */
    public R<Page<Goods>> getGoodsByIds(long current, long size, Collection<String> ids) {
        Page<Goods> goodsPage = new Page<>(current,size);
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        goodsMapper.selectPage(goodsPage,queryWrapper);
        return R.success("success",goodsPage);
    }

    /**
     * 添加商品
     * @param goods 商品类
     * @return Result
     */
    public Result addGoods(Goods goods) {
        goods.setId(IdUtil.getSnowflakeNextIdStr());
        goods.setCreateTime(new Date());
        goods.setUpdateTime(new Date());
        int insertCount =  goodsMapper.insert(goods);
        if(insertCount > 0){
            return Result.success(goods);
        } else {
            return Result.failed(Constants.CODE_400,null);
        }
    }

    /**
     * 更新商品信息
     * @param goods 商品类
     * @return Result
     */
    public Result updateGoods(Goods goods) {
        goods.setUpdateTime(new Date());
        int updateCount =  goodsMapper.updateById(goods);
        if(updateCount > 0){
            return Result.success(null);
        }else {
            return Result.failed(Constants.CODE_400,null);
        }
    }

    /**
     * 删除商品信息
     * @param id 商品ID
     * @return Result
     */
    public Result deleteGoods(List<DeleteParams> id) {
        int deleteCount =  goodsMapper.deleteBatchIds(id);
        if(deleteCount > 0){
            return Result.success(null);
        }else {
            return Result.failed(Constants.CODE_500,null);
        }
    }
}
