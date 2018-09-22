package cn.itcast.bos.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
/**
 * 邮箱工具类
 * @author admin
 *
 */
public class MailUtils {
	//邮箱服务地址
	private static String smtp_host = "smtp.126.com"; 
	//邮箱账号
	private static String username = "sqywan520@126.com"; 
	//授权密码
	private static String password = "abc123"; 

	private static String from = "sqywan520@126.com"; // 使用当前账户
	//激活地址
	public static String activeUrl = "http://localhost:8890/bos_fore/customerAction_activeMail";

	/**
	 * 发送邮件
	 * @param subject 邮件标题
	 * @param content 邮件内容
	 * @param to 收件人
	 */
	public static void sendMail(String subject, String content, String to) {
		//封装属性 邮件地址 邮箱协议 是否需要验证
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		//获取邮件session对象
		Session session = Session.getInstance(props);
		//发送邮件的消息对象
		Message message = new MimeMessage(session);
		try {
			//设置发件人
			message.setFrom(new InternetAddress(from));
			//收件人
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			//标题
			message.setSubject(subject);
			//设置内容  并解决乱码问题
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			//连接邮件  邮箱地址 账号 密码
			transport.connect(smtp_host, username, password);
			//发送邮件  message.getAllRecipients()收件人地址
			transport.sendMessage(message, message.getAllRecipients());
			System.out.println("邮件发送成功了");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

	public static void main(String[] args) {
		sendMail("测试邮件", "你好，传智播客", "wangxin@itcast.cn");
	}
}
