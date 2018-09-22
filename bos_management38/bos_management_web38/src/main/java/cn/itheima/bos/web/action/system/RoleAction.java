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

import cn.itheima.bos.domain.system.Role;
import cn.itheima.bos.domain.system.Role;
import cn.itheima.bos.service.system.IRoleService;
import cn.itheima.bos.web.action.common.CommonAction;

/**
 * 系统管理 -角色模块
 * 
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class RoleAction extends CommonAction<Role> {
	private String menuIds;//菜单ids
	private Integer[] permissionIds;//权限ids
	 
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public void setPermissionIds(Integer[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	@Resource
	private IRoleService roleService;
	
	/*private String id;
	
	public void setId(String id) {
		this.id = id;
	}*/
	// 保存菜单数据
	@Action(value = "roleAction_save", results = {
			@Result(location = "/pages/system/role.html", name = "success", type = "redirect"),
			@Result(location = "/error.html", name = "fail", type = "redirect") })
	public String save() {
		roleService.save(menuIds,permissionIds,model);
		return SUCCESS;
	}
	


	// 分页
	@Action(value = "roleAction_pageQuery")
	public String pageQuery() throws IOException {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Role> pageRole = roleService.pageQuery(pageable);
		this.page2Json(pageRole, new String[] {"users","roles","childrenMenus"});
		return NONE;
	}
	
	
	//查询所有角色信息
	@Action(value = "roleAction_findAll")
	public String findAll() throws IOException {
		List<Role> listRole = roleService.findAll();
		this.list2Json(listRole, new String[]{"users","permissions","menus"});
		return NONE;
	}
}
