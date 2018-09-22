package cn.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.base.Standard;

public interface IStandardService {

	/**
	 * 新增收派标准数据
	 * @param model
	 */
	void save(Standard model);
	
	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	Page<Standard> pageQuery(Pageable pageable);

	/**
	 * 查询收派标准数据
	 * @return
	 */
	List<Standard> findAll();

}
