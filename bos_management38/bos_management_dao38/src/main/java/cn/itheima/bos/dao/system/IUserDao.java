package cn.itheima.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itheima.bos.domain.system.User;

public interface IUserDao extends JpaRepository<User, Integer> {

	/**
	 * 根据用户名查询用户对象
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

}
