package cn.gyk.cartserver.listener;

import cn.gyk.cartserver.config.RabbitMQConfig;
import cn.gyk.cartserver.mapper.CartItemMapper;
import cn.gyk.cartserver.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME) // 使用相同的队列名
public class OrderFinishMessageListener {

    @Resource
    CartItemMapper cartItemMapper;
    @Resource
    private JwtUtil jwtUtil;

    @RabbitHandler
    public void handleOrderFinishMessage(Map<String, Object> message) {
        List<String> goodIdList = (List<String>) message.get("goodIdList");
        String userId = (String) message.get("userId");
        //订单成功支付，删除购物车商品
        if (!goodIdList.isEmpty() || !userId.isEmpty()) {
            int deleteCount = cartItemMapper.deleteByUserAndGoodIds(userId,goodIdList);
            if (deleteCount > 0) {
                System.out.println("Deleted " + deleteCount + " goodIds");
            } else {
                System.out.println("Deleted Error！");
            }
        }
    }
}
