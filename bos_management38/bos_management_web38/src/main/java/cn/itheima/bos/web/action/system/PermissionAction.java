package cn.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itheima.bos.domain.system.Permission;
import cn.itheima.bos.service.system.IPermissionService;
import cn.itheima.bos.web.action.common.CommonAction;

/**
 * 系统管理 -权限模块
 * 
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class PermissionAction extends CommonAction<Permission> {
	@Resource
	private IPermissionService permissionService;

	 
	// 保存菜单数据
	@Action(value = "permissionAction_save", results = {
			@Result(location = "/pages/system/permission.html", name = "success", type = "redirect"),
			@Result(location = "/error.html", name = "fail", type = "redirect") })
	public String save() {
		permissionService.save(getModel());
		return SUCCESS;
	}

	// 分页
	@Action(value = "permissionAction_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Permission> pagePermission = permissionService.pageQuery(pageable);
		this.page2Json(pagePermission, new String[] { "roles"});
		return NONE;
	}
	
	
	//查询所有权限数据
	@Action(value = "permissionAction_findAll")
	public String findAll() throws IOException {
		List<Permission> listPermisson = permissionService.findAll();
		this.list2Json(listPermisson, new String[]{"roles"});
		return NONE;
	}
}
