package cn.itheima.bos.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.system.IPermissionDao;
import cn.itheima.bos.domain.system.Permission;
import cn.itheima.bos.service.system.IPermissionService;
/**
 * 权限模块（业务逻辑处理层接口实现类）
 * @author admin
 *
 */
@Service
@Transactional
public class PermissionServiceImpl implements IPermissionService {

	@Resource
	private IPermissionDao permissionDao;
	
	@Override
	public void save(Permission model) {
		permissionDao.save(model);
	}

	@Override
	public Page<Permission> pageQuery(Pageable pageable) {
		return permissionDao.findAll(pageable);
	}

	@Override
	public List<Permission> findAll() {
		return permissionDao.findAll();
	}

}
