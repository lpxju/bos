package cn.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.system.Menu;

public interface IMenuDao extends JpaRepository<Menu, Integer> {

	/**
	 * 查询父菜单数据  通过立即加载的方式将子菜单数据查询出来
	 * @return
	 */
	List<Menu> findByParentMenuIsNull();

	/**
	 * jpql 根据用户id查询对应菜单数据
	 * @param id
	 * @return
	 */
	@Query(value="select m from Menu m inner join m.roles r inner join r.users u where u.id = ?")
	List<Menu> findMenuByUserId(int id);

}
