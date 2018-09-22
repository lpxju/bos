package cn.itheima.bos.web.action.system;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itheima.bos.domain.system.Role;
import cn.itheima.bos.domain.system.User;
import cn.itheima.bos.service.system.IUserService;
import cn.itheima.bos.web.action.common.CommonAction;
/**
 * 系统管理 -用户模块
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class UserAction extends CommonAction<User> {
	
	
	@Resource
	private IUserService userService;
	
	private Integer[] roleIds;//角色ids
	public void setRoleIds(Integer[] roleIds) {
		this.roleIds = roleIds;
	}

	private String checkCode;//前台输入的验证码
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	//保存
	@Action(value="userAction_login",results={
			@Result(location="/index.html",name="success",type="redirect"),
			@Result(location="/login.html",name="fail",type="redirect")
	})
	public String login(){
		//获取页面上输入的验证码checkCode  
		//获取session中的验证码 key
		String sessionCheckCode = (String)ServletActionContext.getRequest().getSession().getAttribute("key");
		if(StringUtils.isNoneBlank(checkCode,sessionCheckCode,getModel().getUsername(),getModel().getPassword()) && checkCode.equals(sessionCheckCode)){
			//通过shiro框架提供的login方法登录
			try {
				Subject subject = SecurityUtils.getSubject();//用户对象
				AuthenticationToken token = new UsernamePasswordToken(getModel().getUsername(), getModel().getPassword());
				subject.login(token);//登录失败则抛出异常
				//将用户对象放入session
				User user = (User)subject.getPrincipal();//用户对象  
				ServletActionContext.getRequest().getSession().setAttribute("user", user);
				return SUCCESS;
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "fail";
	}
	//登出
	@Action(value="userAction_logout",results={
			@Result(location="/login.html",name="success",type="redirect")
	})
	public String logout(){
		//用户对象
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}
	
	//保存用户功能
	@Action(value="userAction_save",results={
			@Result(location="/pages/system/userlist.html",name="success",type="redirect"),
			@Result(location="/error.html",name="error",type="redirect")
	})
	public  String save(){
		userService.save(getModel(),roleIds);
		return SUCCESS;
	}
	
	//分页
	@Action(value = "userAction_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<User> pageRole = userService.pageQuery(pageable);
		this.page2Json(pageRole, new String[] { "roles"});
		return NONE;
	}
	

}
