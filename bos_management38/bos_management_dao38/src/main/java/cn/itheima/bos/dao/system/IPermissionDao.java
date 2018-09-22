package cn.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.system.Permission;

public interface IPermissionDao extends JpaRepository<Permission, Integer> {

	/**
	 * 根据用户id查询权限数据
	 * @param id
	 * @return
	 */
	@Query(value="select p from Permission p inner join p.roles r inner join r.users u where u.id = ?")
	List<Permission> findPermissionByUserId(int id);

}
