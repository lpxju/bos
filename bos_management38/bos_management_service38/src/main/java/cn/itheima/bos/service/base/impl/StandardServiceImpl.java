package cn.itheima.bos.service.base.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.IStandardDao;
import cn.itheima.bos.domain.base.Standard;
import cn.itheima.bos.service.base.IStandardService;
/**
 * 业务逻辑处理层  收派标准
 * @author admin
 *
 */
@Transactional
@Service
public class StandardServiceImpl implements IStandardService {

	@Resource
	private IStandardDao standardDao;
	
	@Override
	public void save(Standard standard) {
		standardDao.save(standard);
	}

	@Override
	public Page<Standard> pageQuery(Pageable pageable) {
		return standardDao.findAll(pageable);
	}

	@Override
	public List<Standard> findAll() {
		return standardDao.findAll();
	}

}
