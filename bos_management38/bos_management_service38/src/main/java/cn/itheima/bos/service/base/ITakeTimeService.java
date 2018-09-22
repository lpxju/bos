package cn.itheima.bos.service.base;

import java.util.List;

import cn.itheima.bos.domain.base.TakeTime;

public interface ITakeTimeService {

	/**
	 * 查询所有收派时间
	 * @return
	 */
	List<TakeTime> listajax();

}
