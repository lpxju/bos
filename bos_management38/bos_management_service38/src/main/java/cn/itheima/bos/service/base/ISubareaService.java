package cn.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.base.SubArea;
import cn.itheima.bos.domain.column.ColumnData;
import cn.itheima.bos.domain.column.Data;

public interface ISubareaService {

	/**
	 * 保存分区数据
	 * @param model
	 */
	void save(SubArea model);

	/**
	 * 下载分区所有数据
	 */
	void exportXls();

	/**
	 * 分区分布图
	 * @return
	 */
	List<Object[]> doPie();

	List<Data> doColumnLevelOne();

	List<ColumnData> doColumnLevelTwo();

	Page<SubArea> pageQuery(Pageable pageable);

	List<SubArea> findSubareaByFixedAreaId(String id);

}
