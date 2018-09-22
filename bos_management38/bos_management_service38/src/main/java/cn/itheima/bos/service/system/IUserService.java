package cn.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.system.User;

public interface IUserService {

	/**
	 * 新增用户   
	 * 往用户角色中间表插入数据
	 * @param model
	 * @param roleIds
	 */
	void save(User model, Integer[] roleIds);

	Page<User> pageQuery(Pageable pageable);

}
