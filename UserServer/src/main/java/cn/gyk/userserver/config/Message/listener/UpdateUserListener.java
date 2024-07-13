package cn.gyk.userserver.config.Message.listener;

import cn.gyk.userserver.config.Message.UpdateUserInfo;
import cn.gyk.userserver.web.EmailUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserListener {
    @Resource
    private EmailUtil emailUtil;

    @RabbitListener(queues = UpdateUserInfo.QUEUE_NAME)
    public void sendCodeEmail(String message) {
        // 解析消息以获取收件人邮箱地址
        String[] parts = message.split(":");
        if (parts.length == 2) {
            String to = parts[1].trim();// 使用从消息中获取的邮箱地址
            // 执行邮件发送操作
            String subject = "更改密码验证邮件";// 邮件主题
            String content = "查询成功" + IdUtil.fastUUID();// 邮件内容

            emailUtil.sendCodeEmail(to, subject, content);
        }
    }
}
