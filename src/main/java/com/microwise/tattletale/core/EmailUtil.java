package com.microwise.tattletale.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件工具类
 *
 * @author sun.cong
 * @create 2017-11-30 15:52
 **/
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送邮件
     *
     * @param recipient 发送对象邮箱
     * @param text      邮件内容
     * @throws MessagingException
     */
    public void prepareAndSend(String recipient, String subject, String text) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("no-reply-terminator@microwise-system.com");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
    }

    /**
     * 获取org.thymeleaf.context.Context实例
     *
     * @return
     */
    public static Context getContextInstance() {
        return new Context();
    }
}
