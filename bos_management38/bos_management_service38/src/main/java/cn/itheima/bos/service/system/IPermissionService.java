package cn.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.system.Permission;

public interface IPermissionService {

	void save(Permission model);

	Page<Permission> pageQuery(Pageable pageable);

	/**
	 * 角色管理-查询所有权限数据
	 * @return
	 */
	List<Permission> findAll();

}
