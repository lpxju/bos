package cn.itheima.bos.service.system.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.system.IUserDao;
import cn.itheima.bos.domain.system.Role;
import cn.itheima.bos.domain.system.User;
import cn.itheima.bos.service.system.IUserService;
/**
 * 用户模块（业务逻辑处理层接口实现类）
 * @author admin
 *
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

	@Resource
	private IUserDao userDao;
	
	@Override
	public void save(User user, Integer[] roleIds) {
		//保存用户数据
		userDao.save(user);
		//保存用户角色中间表
		if(roleIds != null && roleIds.length > 0){
			for (Integer roleId : roleIds) {
				Set<Role> roles = user.getRoles();//获取中间表
				Role role = new Role();
				role.setId(roleId);
				roles.add(role);//将用户角色中间表插入数据
			}
		}
	}

	@Override
	public Page<User> pageQuery(Pageable pageable) {
		return userDao.findAll(pageable);
	}

}
