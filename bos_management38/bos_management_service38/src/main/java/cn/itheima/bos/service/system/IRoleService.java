package cn.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.system.Role;

public interface IRoleService {

	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	Page<Role> pageQuery(Pageable pageable);

	/**
	 * 保存角色数据   
	 * 保存角色菜单中间表
	 * 保存角色权限中间表
	 * @param menuIds
	 * @param permissionIds
	 * @param model
	 */
	void save(String menuIds, Integer[] permissionIds, Role model);

	/**
	 * 查询所有角色数据
	 * @return
	 */
	List<Role> findAll();

}
