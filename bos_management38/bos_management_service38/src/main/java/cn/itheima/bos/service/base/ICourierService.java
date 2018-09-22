package cn.itheima.bos.service.base;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itheima.bos.domain.base.Courier;

public interface ICourierService {

	/**
	 * 保存
	 * @param model
	 */
	void save(Courier model);

	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	Page<Courier> pageQuery(Pageable pageable);
	
	/**
	 * 带条件的分页查询
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<Courier> pageQuery(Specification<Courier> spec, Pageable pageable);

	/**
	 * 需要的删除快递员ids
	 * @param ids
	 * @param param 
	 */
	@RequiresPermissions("delete")
	void delete(String ids, Integer param);

	/**
	 * 定区关联快递员 查询未作废的快递员数据
	 * @return
	 */
	List<Courier> listajx();

	/*void restore(String ids);*/

}
