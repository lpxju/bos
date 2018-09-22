package cn.itheima.bos.service.base.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.ICourierDao;
import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.service.base.ICourierService;
/**
 * 业务逻辑处理层  快递员
 * @author admin
 *
 */
@Transactional
@Service
public class CourierServiceImpl implements ICourierService {

	@Resource
	private ICourierDao courierDao;
	
	@Override
	public void save(Courier courier) {
		courierDao.save(courier);
	}

	@Override
	public Page<Courier> pageQuery(Pageable pageable) {
		return courierDao.findAll(pageable);
	}

	public void delete(String ids,Integer param) {
		//一定做后台校验 
		if(StringUtils.isNotBlank(ids)){
			String[] courierIds = ids.split(",");
			for (String courierId : courierIds) {
				//更新快递员状态
				courierDao.updateByCourierId(Integer.parseInt(courierId),param);
			}
		}
		
	}

	//带条件分页查询
	@Override
	public Page<Courier> pageQuery(Specification<Courier> spec, Pageable pageable) {
		return courierDao.findAll(spec, pageable);
	}

	@Override
	public List<Courier> listajx() {
		return courierDao.findByDeltagIsNull();
	}

	/*@Override
	public void restore(String ids) {
		if(StringUtils.isNotBlank(ids)){
			String[] courierIds = ids.split(",");
			for (String courierId : courierIds) {
				//更新快递员状态
				courierDao.updateByCourierId(Integer.parseInt(courierId));
			}
		}
	}*/

}
