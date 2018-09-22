package cn.itheima.bos.service.base.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.ITakeTimeDao;
import cn.itheima.bos.domain.base.TakeTime;
import cn.itheima.bos.service.base.ITakeTimeService;
/**
 * 业务逻辑处理层  收派时间
 * @author admin
 *
 */
@Transactional
@Service
public class TakeTimeServiceImpl implements ITakeTimeService {

	@Resource
	private ITakeTimeDao takeTimeDao;
	
	@Override
	public List<TakeTime> listajax() {
		return takeTimeDao.findAll();
	}

}
