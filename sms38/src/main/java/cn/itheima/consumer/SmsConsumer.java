package cn.itheima.consumer;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

/**
 * 消费者 读取消息发送短信给用户
 * @author admin
 *
 */
@Component
public class SmsConsumer implements MessageListener{
	
	@Override
	public void onMessage(Message message) {
		try {
			
			/**
			 * 以后项目中重点需要关注点 需要编写复杂业务逻辑
			 */
			MapMessage mapMessage=(MapMessage)message;
			String telephone = mapMessage.getString("telephone");
			String content = mapMessage.getString("content");
			//调用短信接口，发送短信给用户
			System.out.println("手机号："+telephone+"短信内容："+content);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
