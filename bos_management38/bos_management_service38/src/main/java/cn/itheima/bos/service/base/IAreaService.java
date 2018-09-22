package cn.itheima.bos.service.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.base.Area;

public interface IAreaService {

	/**
	 * 文件上传 导入区域数据
	 * @param areaFile
	 */
	String importXls(File areaFile);

	/**
	 * 分页
	 * @param pageable
	 * @return
	 */
	Page<Area> pageQuery(Pageable pageable);

	/**
	 * 查询所有区域数据
	 * @return
	 */
	List<Area> findAll();

	/**
	 * 根据q进行模糊查询
	 * @param string
	 * @return
	 */
	List<Area> findByQ(String string);

}
