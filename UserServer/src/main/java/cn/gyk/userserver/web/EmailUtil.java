package cn.gyk.userserver.web;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {
    @Resource
    private JavaMailSender mailSender;

    //配置文件中的发送QQ邮箱
    @Value("${mail.username}")
    private String from;


    /**
     * 验证码邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendCodeEmail(String to, String subject, String content) {
        //获取MimeMessage对象
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            //邮件发送人
            mimeMessageHelper.setFrom(from);
            //邮件接收人
            mimeMessageHelper.setTo(to);
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容，html格式
            mimeMessageHelper.setText(content, true);
            //发送
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
