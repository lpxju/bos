package cn.itheima.bos.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import cn.itheima.bos.dao.take_delivery.IWorkBillDao;
import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.domain.take_delivery.WorkBill;

/**
 * 自定义任务类 
 * 需求：查询所有的工单数据 ，统计以邮件形式发送给管理账号xxx
 * @author admin
 *
 */
public class WorkBillJobs {
	
	@Resource
	private IWorkBillDao workBillDao; //工单dao
	
	@Resource(name="jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	
	/**
	 * 任务类方法没有返回值
	 * 
	 * 发送邮件方法
	 */
	public void sendMail(){
		//将工单所有数据查询出来
		List<WorkBill> listWorkBill = workBillDao.findAll();
		if(listWorkBill != null && listWorkBill.size() > 0){
			String  content = "<html><body><table border='1px'><tr><td>工单编号</td><td>工单类型</td><td>取件状态</td><td>快递员</td></tr>";//邮件内容
			for (WorkBill workBill : listWorkBill) {
				Courier courier = workBill.getCourier();
				String courierName = "";
				if(courier != null){
					courierName = courier.getName();
				}
				//拼接table表格，以邮件形式发送给管理员
				content +="<tr><td>"+workBill.getId()+"</td><td>"+workBill.getType()+"</td><td>"+workBill.getPickstate()+"</td><td>"+courierName+"</td></tr>";
			}
			
			content +="</table></body></html>";
			String to = "quzhitao@yql.com";
			String subject = "工单统计";
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("subject", subject);
			map.put("content", content);
			map.put("to", to);
			this.send("mail_message", map);
		}
		
	}
	
	public void send(String queueName,final Map<String,String> map){
		jmsTemplate.send(queueName,new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				
				for (String key : map.keySet()) {
					message.setString(key, map.get(key));
				}
				return message;
			}
		});
	}

}
