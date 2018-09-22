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

import cn.itheima.bos.domain.base.FixedArea;
import cn.itheima.bos.domain.system.Menu;
import cn.itheima.bos.service.system.IMenuService;
import cn.itheima.bos.web.action.common.CommonAction;

/**
 * 系统管理 -菜单模块
 * 
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class MenuAction extends CommonAction<Menu> {
	@Resource
	private IMenuService menuService;

	// 查询所有菜单数据
	@Action(value = "menuAction_findAllMenu")
	public String findByParentMenuIsNull() throws IOException {
		List<Menu> menuList = menuService.findByParentMenuIsNull();
		this.list2Json(menuList, new String[] { "roles", "childrenMenus", "parentMenu" });
		return NONE;
	}

	// 保存菜单数据
	@Action(value = "menuAction_save", results = {
			@Result(location = "/pages/system/menu.html", name = "success", type = "redirect"),
			@Result(location = "/error.html", name = "fail", type = "redirect") })
	public String save() {
		menuService.save(getModel());
		return SUCCESS;
	}

	// 分页
	@Action(value = "menuAction_pageQuery")
	public String pageQuery() throws IOException {
		//页面传入的page会将值存入实体类中page中，将page值获取放入属性驱动中page
		String page2 = getModel().getPage();
		Pageable pageable = new PageRequest(Integer.parseInt(page2) - 1, rows);
		Page<Menu> pageMenu = menuService.pageQuery(pageable);
		this.page2Json(pageMenu, new String[] { "roles", "childrenMenus", "parentMenu","children" });
		return NONE;
	}
	
	//根据用户id查询对应菜单数据 展示
	//menuAction_findMenuByUserId
	@Action(value = "menuAction_findMenuByUserId")
	public String findMenuByUserId() throws IOException {
		
		List<Menu> listMenu = menuService.findMenuByUserId(); 
		this.list2Json(listMenu, new String[]{"roles","childrenMenus","parentMenu","children"});//id pId name 
		return NONE;
	}
	
}
