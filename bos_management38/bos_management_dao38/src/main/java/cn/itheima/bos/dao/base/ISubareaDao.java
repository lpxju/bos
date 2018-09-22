package cn.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.base.SubArea;
import cn.itheima.bos.domain.column.Data;

public interface ISubareaDao extends JpaRepository<SubArea, String> {

	/**
	 * 根据区域id 查询对应的分区数据
	 * @param id
	 * @return
	 */
	@Query(value="select * from t_sub_area t where t.c_area_id = ?",nativeQuery=true)
	List<SubArea> findSubareaById(String id);

	
	/**
	 * 分区分布图sql
	 * @return
	 */
	@Query(value="select ta.c_province,count(*) from t_sub_area tsa inner join t_area ta on tsa.c_area_id = ta.c_id group by ta.c_province",nativeQuery=true)
	List<Object[]> doPie();
	
	
	
	@Query(value="select ta.c_province,count(*) from t_area ta inner join t_sub_area tsa on tsa.c_area_id = ta.c_id group by ta.c_province",nativeQuery=true)
	List<Object[]> doColumnLevelOne();
	
	
	@Query(value="select ta.c_province,ta.c_city,count(*) from t_area ta inner join t_sub_area tsa on tsa.c_area_id = ta.c_id group by ta.c_city,ta.c_province",nativeQuery=true)
	List<Object[]> doColumnLevelTwo();
	
	@Query(value=" select ta.c_city,ta.c_district,count(*) from t_area ta inner join t_sub_area tsa on tsa.c_area_id = ta.c_id group by ta.c_city,ta.c_district",nativeQuery=true)
	List<Object[]> doColumnLevelThree();
	
	@Query(value="select * from t_sub_area where c_fixedarea_id = ?",nativeQuery=true)
	List<SubArea> findSubareaByFixedAreaId(String id);
}
