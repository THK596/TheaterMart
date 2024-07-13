package cn.gyk.orderserver.service.impl;

import cn.gyk.commonserver.domain.dto.DeleteParams;
import cn.gyk.commonserver.utils.Constants;
import cn.gyk.commonserver.utils.R;
import cn.gyk.commonserver.utils.Result;
import cn.gyk.orderserver.config.RabbitMQConfig;
import cn.gyk.orderserver.domain.dto.AddOrderParams;
import cn.gyk.orderserver.domain.dto.OrderGoodsDto;
import cn.gyk.orderserver.domain.entity.Order;
import cn.gyk.orderserver.domain.vo.OrderDetailsVo;
import cn.gyk.orderserver.mapper.AddOrderListMapper;
import cn.gyk.orderserver.mapper.OrderListMapper;
import cn.gyk.orderserver.mapper.OrderMapper;
import cn.gyk.orderserver.utils.JwtUtil;
import cn.gyk.tmapi.client.GoodsClient;
import cn.gyk.tmapi.dto.GoodsDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class OrderInfoServiceImpl {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    OrderListMapper orderListMapper;
    @Resource
    private GoodsClient goodsClient;
    @Resource
    OrderMapper orderMapper;
    @Resource
    AddOrderListMapper addOrderListMapper;
    @Resource
    private AmqpTemplate amqpTemplate;

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
     * 获取用户订单商品详情
     * @param current
     * @param size
     * @return
     */
    public R<Page<OrderGoodsDto>> getOrderList(long current, long size) {
        // 获取登录用户ID
        String userId = getUserInfo();

        // 获取登录订单列表
        List<OrderDetailsVo> orderDetailsVoList = orderListMapper.GetOrderList(userId);

        // 提取orderDetailsVoList的good_id并添加到ids集合中
        Set<String> ids = new HashSet<>();
        for (OrderDetailsVo orderDetailsVo : orderDetailsVoList) {
            ids.add(orderDetailsVo.getGoodId());
        }

        // 如果ids 为空，则可能不需要继续查询商品信息
        if(ids.isEmpty()) {
            // 返回空列表或者包含特定错误信息的响应
            return R.error("购物车中没有商品");
        }

        // 计算总页数
        long totalPages = (orderDetailsVoList.size() + size - 1) / size;

        // 处理 current 超出范围的情况
        if (current > totalPages) {
            return R.error("没有更多数据");
        }

        // 计算分页的起始和结束索引
        int start = (int) ((current - 1) * size);
        int end = Math.min(start + (int) size, orderDetailsVoList.size());
        List<OrderDetailsVo> paginatedOrderDetails = orderDetailsVoList.subList(start, end);

        // 使用分页后的订单信息获取商品信息
        R<Page<GoodsDto>> goodsResponse = goodsClient.getGoodsByIds(1, Integer.MAX_VALUE, ids);
        // 获取所有商品信息，避免分页导致部分商品无法匹配

        Page<GoodsDto> goodsPage = goodsResponse.getData();
        List<GoodsDto> goodsList = goodsPage.getRecords();

        // 将订单详细信息和商品详细信息结合
        List<OrderGoodsDto> orderGoodsDtoList = new ArrayList<>();
        for (OrderDetailsVo orderDetailsVo : paginatedOrderDetails) {
            // 使用分页后的订单列表
            for (GoodsDto goodsDto : goodsList) {
                if (orderDetailsVo.getGoodId().equals(goodsDto.getId())) {
                    OrderGoodsDto orderGoodsDto = new OrderGoodsDto();
                    orderGoodsDto.setId(orderDetailsVo.getId());
                    orderGoodsDto.setUserId(orderDetailsVo.getUserId());
                    orderGoodsDto.setTotalPrice(String.valueOf(orderDetailsVo.getTotalPrice()));
                    orderGoodsDto.setStatus(orderDetailsVo.getStatus());
                    orderGoodsDto.setCreateTime(orderDetailsVo.getCreateTime());
                    orderGoodsDto.setQuantity(Integer.valueOf(orderDetailsVo.getQuantity()));
                    orderGoodsDto.setOrderPrice(String.valueOf(orderDetailsVo.getOrderPrice()));
                    orderGoodsDto.setGoodName(goodsDto.getGoodName());
                    orderGoodsDto.setCategory(goodsDto.getCategory());
                    orderGoodsDto.setPrice(goodsDto.getPrice());
                    orderGoodsDtoList.add(orderGoodsDto);
                    break;
                }
            }
        }

        // 创建分页对象
        Page<OrderGoodsDto> orderGoodsPage = new Page<>(current, size, orderDetailsVoList.size()); // 使用完整订单列表的size
        orderGoodsPage.setRecords(orderGoodsDtoList);
        return R.success("success", orderGoodsPage);
    }

    /**
     * 更新订单信息
     * @param order 订单类
     * @return Result
     */
    public Result updateOrder(Order order) {
        order.setUpdateTime(new Date());
        int updateCount = orderMapper.updateById(order);
        if(updateCount > 0) {
            return Result.success(null);
        } else {
            return Result.failed(Constants.CODE_500,null);
        }
    }

    /**
     * 条件批量删除订单信息
     * @param id
     * @return Result
     */

    // TODO 已完成订单/取消订单状态才能删除
    public Result deleteOrder(List<DeleteParams> id) {
        int deleteCount = orderMapper.deleteBatchIds(id);
        if(deleteCount > 0) {
            return Result.success(null);
        } else {
            return Result.failed(Constants.CODE_500,null);
        }
    }

    /**
     * 添加订单
     * @param addOrderParamsList
     * @return Result
     */
    public Result addOrder(List<AddOrderParams> addOrderParamsList) {
        // 获取登录用户ID
        String userId = getUserInfo();

        Date now = new Date();

        // 1. 计算总价
        double totalPrice = addOrderParamsList.stream()
                .mapToDouble(AddOrderParams::getOrderPrice)
                .sum();

        // 2. 创建新的 order
        Order order = new Order();
        order.setUserId(userId);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        order.setTotalPrice(totalPrice);
        order.setStatus(String.valueOf(1)); // 假设 1 代表订单状态为 "新建"
        order.setId(IdUtil.getSnowflakeNextIdStr());
        orderMapper.insert(order); // 插入 order 表

        // 3. 获取 orderId
        String orderId = order.getId();

        // 4. 批量插入数据到 order_details 表

        addOrderParamsList.forEach(addOrderParams -> {
            addOrderParams.setOrderId(orderId);
            addOrderParams.setCreateTime(now);
            addOrderParams.setUpdateTime(now);
            addOrderParams.setId(IdUtil.getSnowflakeNextIdStr());
        });
        addOrderListMapper.saveBatch(addOrderParamsList);

        // 5. 发送消息队列
        List<String> goodIdList = addOrderParamsList.stream()
                .map(AddOrderParams::getGoodId)
                .toList();

        Map<String,Object> message = new HashMap<>();
        message.put("goodIdList",goodIdList);
        message.put("userId",userId);

        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,message);


        return Result.success(null);
    }

    public R<Page<Order>> getAllOrderList(long current, long size) {
        // 获取登录用户ID
        String userId = getUserInfo();
        Page<Order> orderPage = new Page<>(current, size);
//        long total = orderPage.getTotal();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId); // 设置userId为查询条件

        orderMapper.selectPage(orderPage, queryWrapper);
        return R.success("success", orderPage);
    }

}
