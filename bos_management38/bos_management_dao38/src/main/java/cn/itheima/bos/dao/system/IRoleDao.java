package cn.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.system.Role;

public interface IRoleDao extends JpaRepository<Role, Integer> {

	/**
	 * 根据用户id查询角色数据
	 * @param id
	 * @return
	 */
	@Query(value="select r from Role r inner join  r.users u where u.id = ?") //jpql
	List<Role> findRoleByUserId(int id);

}
