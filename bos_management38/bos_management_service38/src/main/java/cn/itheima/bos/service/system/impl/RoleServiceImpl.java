package cn.itheima.bos.service.system.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.system.IRoleDao;
import cn.itheima.bos.domain.system.Menu;
import cn.itheima.bos.domain.system.Permission;
import cn.itheima.bos.domain.system.Role;
import cn.itheima.bos.service.system.IRoleService;
/**
 * 角色模块（业务逻辑处理层接口实现类）
 * @author admin
 *
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

	@Resource
	private IRoleDao roleDao;
	
	@Override
	public Page<Role> pageQuery(Pageable pageable) {
		return roleDao.findAll(pageable);
	}

	@Override
	public void save(String menuIds, Integer[] permissionIds, Role role) {
		
		
		//保存角色菜单中间表
		Set<Menu> menus = role.getMenus(); //得到中间表
		if(StringUtils.isNotBlank(menuIds)){
			String[] menuIds2 = menuIds.split(",");
			for (String menuId : menuIds2) {
				Menu menu = new Menu();
				menu.setId(Integer.parseInt(menuId));
				menus.add(menu);//set:更新某一个字段值 add往表中插入数据
			}
		}
		
		//保存角色权限中间表
		Set<Permission> permissions = role.getPermissions();//得到中间表
		if(permissionIds != null && permissionIds.length > 0){
			for (Integer permissionId : permissionIds) {
				Permission p = new Permission();
				p.setId(permissionId);
				permissions.add(p);//往 角色权限中间表插入数据
			}
		}
		
		//先保存角色数据
		roleDao.save(role);
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAll();
	}

}
