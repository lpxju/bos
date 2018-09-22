package cn.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.base.Courier;

public interface ICourierDao extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier> {

	@Query(value="update t_courier set c_deltag = ?2 where c_id = ?1",nativeQuery=true)//sql语句
	@Modifying //update delete 改变数据时候加上此注解 告知spring data jpa默认事务只读状态
	void updateByCourierId(Integer courierIed, Integer param);

	/**
	 * 定区关联快递员 查询未作废的快递员数据
	 * @return
	 */
	List<Courier> findByDeltagIsNull();

}
