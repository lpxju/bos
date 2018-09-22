package cn.itheima.workbill;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.MessagingException;

import org.springframework.stereotype.Component;

import cn.itheima.utils.MailUtils;


@Component
public class MailWorkBill implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage)message;
			String subject = mapMessage.getString("subject");
			String content = mapMessage.getString("content");
			String to = mapMessage.getString("to");
			MailUtils.sendMail(subject, content, to);
		} catch (JMSException | MessagingException e) {
			e.printStackTrace();
		}
	}

}
