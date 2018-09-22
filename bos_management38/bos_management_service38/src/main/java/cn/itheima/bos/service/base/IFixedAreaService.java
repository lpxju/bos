package cn.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.base.FixedArea;

public interface IFixedAreaService {

	void save(FixedArea model);

	/**
	 * 分页查询
	 * @param pageable
	 * @return
	 */
	Page<FixedArea> pageQuery(Pageable pageable);

	/**
	 * 定区关联快递员 （往定区快递员中间表插入数据）
	 * 快递员跟收派时间 更新快递员表中收派时间id
	 * @param id
	 * @param courierId
	 * @param takeTimeId
	 */
	void associationCourierToFixedArea(String id, Integer courierId, Integer takeTimeId);

	FixedArea findCourierByFixedAreaId(String id);
}
