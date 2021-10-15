package com.example.epidemicsurveillance.utils.rabbitmq.spider;

import com.example.epidemicsurveillance.config.rabbitmq.MailConstants;
import com.example.epidemicsurveillance.entity.vo.EmailVo;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @ClassName SpiderErrorSendMailToAdmin
 * @Author 朱云飞
 * @Date 2021/10/14 17:40
 * @Version 1.0
 **/
@Component
public class SpiderErrorSendMailToAdmin {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailToAdmin(String email,String message){
        EmailVo vo=new EmailVo();
        vo.setAdminEmail(email);
        vo.setEmailMessage(message);
        //消息唯一Id
        String msgId = UUID.randomUUID().toString();
        //发送交换机，路由键，消息内容和用户邮箱,消息ID
        rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                MailConstants.MAIL_ROUTING_KEY_NAME,
                vo,
                new CorrelationData(msgId));
    }
}
