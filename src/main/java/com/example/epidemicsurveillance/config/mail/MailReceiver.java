package com.example.epidemicsurveillance.config.mail;

import com.example.epidemicsurveillance.config.rabbitmq.MailConstants;
import com.example.epidemicsurveillance.entity.vo.EmailVo;
import com.example.epidemicsurveillance.exception.EpidemicException;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;


/**
 * @ClassName MailConfig
 * @Author 朱云飞
 * @Date 2021/9/12 13:53
 * @Version 1.0
 **/
@Component
public class MailReceiver {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    //拿取Message 和 channel 可以拿到 消息序号鉴别消息是否统一个消息多收    通过消息序号+msgId两个来鉴别
    public void handler(Message message, Channel channel) {
            EmailVo vo = (EmailVo) message.getPayload();
            MessageHeaders headers = message.getHeaders();
            //消息序号
            long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            //拿到存取消息唯一标识
            String msgId = (String) headers.get("spring_returned_message_correlation");//这个key固定
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            //从Redis中拿取，如果存在，说明消息已经发送成功了，这里直接确认返回
            if (operations.get(msgId) != null){
                LOGGER.error("消息已经被消费=============>{}",msgId);
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否确认多条
                 */
                channel.basicAck(tag,false);
                return;
                }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(vo.getAdminEmail());
            //主题
            helper.setSubject(vo.getEmailMessage());
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            //用于theymeleaf获取
            context.setVariable("messageCode", vo.getEmailMessage());
            //将准备好的theymeleaf模板中的信息转为String
            //String mail = templateEngine.process("mail", context);
            String mail=vo.getEmailMessage();
            helper.setText(mail, true);
            //发送邮件
            javaMailSender.send(msg);
            //手动确认消息
            channel.basicAck(tag, false);
            LOGGER.info("邮件发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * 手动确认消息
             * tag：消息序号
             * multiple：是否确认多条
             * requeue：是否退回到队列
             */
            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                ex.printStackTrace();
                LOGGER.error("邮件发送失败=========>{}", e.getMessage());
            }
            LOGGER.error("邮件发送失败=========>{}", e.getMessage());
            throw new EpidemicException("邮件发送失败");
        }
    }
}
