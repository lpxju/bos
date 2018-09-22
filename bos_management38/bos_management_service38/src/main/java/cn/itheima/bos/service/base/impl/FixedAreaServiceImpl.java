package cn.itheima.bos.service.base.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.ICourierDao;
import cn.itheima.bos.dao.base.IFixedAreaDao;
import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.domain.base.FixedArea;
import cn.itheima.bos.domain.base.TakeTime;
import cn.itheima.bos.service.base.IFixedAreaService;
/**
 * 业务逻辑处理层  定区
 * @author admin
 *
 */
@Transactional
@Service
public class FixedAreaServiceImpl implements IFixedAreaService {

	@Resource
	private IFixedAreaDao fixedAreaDao;
	
	@Resource
	private ICourierDao courierDao; 
	
	
	@Override
	public void save(FixedArea model) {
		//设置uuid 为主键id
		model.setId(UUID.randomUUID().toString());
		fixedAreaDao.save(model);
	}

	@Override
	public Page<FixedArea> pageQuery(Pageable pageable) {
		return fixedAreaDao.findAll(pageable);
	}

	@Override
	public void associationCourierToFixedArea(String id, Integer courierId, Integer takeTimeId) {
		//定区快递员 多对多 
		///通过定区得到中间表 最终往中间表插入关系
		//根据定区id得到定区持久对象  
		FixedArea fixedArea = fixedAreaDao.findOne(id);//持久态对象
		///得到快递员对象
		//Courier courier = new Courier();  //持久(快递员还需跟收派时间产生关系  从数据库查询出来)     或 托管状态 一定要id
		//courier.setId(courierId);
		Courier courier = courierDao.findOne(courierId);
		fixedArea.getCouriers().add(courier); //得到快递员跟定区中间表    
		TakeTime takeTime = new TakeTime();//只有有id
		takeTime.setId(takeTimeId);
		courier.setTakeTime(takeTime);//设置属性 更新收派时间id
	}
	
	@Override
	public FixedArea findCourierByFixedAreaId(String id) {
		return fixedAreaDao.findById(id);
	}
	
}
