package cn.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.system.Menu;

public interface IMenuService {

	/**
	 * 查询顶级查询  子菜单数据通过立即加载方式来查询
	 * @return
	 */
	List<Menu> findByParentMenuIsNull();

	/**
	 * 保存菜单数据
	 * @param model
	 */
	void save(Menu model);

	/**
	 * 分页
	 * @param pageable
	 * @return
	 */
	Page<Menu> pageQuery(Pageable pageable);

	/**
	 * 根据用户查询菜单数据
	 * @return
	 */
	List<Menu> findMenuByUserId();

}
