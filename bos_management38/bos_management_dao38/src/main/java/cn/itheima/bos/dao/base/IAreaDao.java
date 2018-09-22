package cn.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.base.Area;

public interface IAreaDao extends JpaRepository<Area, String> {

	@Query(value="select * from t_area t where t.c_city like ?1 or upper(t.c_citycode) like ?1 or t.c_district like ?1 or t.c_province like ?1 or t.c_shortcode like ?1",nativeQuery=true)
	List<Area> findByQ(String q);

	/**
	 * 根据省市区查询区域对象
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	Area findByProvinceAndCityAndDistrict(String province, String city, String district);

}
