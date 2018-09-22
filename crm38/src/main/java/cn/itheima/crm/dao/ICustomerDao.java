package cn.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.crm.domain.Customer;

public interface ICustomerDao extends JpaRepository<Customer, Integer> {

	//左侧：查询未关联定区的客户数据
	public List<Customer> findByFixedAreaIdIsNull();
	
	//右侧：查询已经关联定区的客户数据
	public List<Customer> findByFixedAreaId(String fixedAreaId);
	
	/**
	 * 清除当前定区已经关联的客户
	 * @param fixedAreaId
	 */
	@Query(value="update t_customer set C_FIXED_AREA_ID = '' where C_FIXED_AREA_ID = ?",nativeQuery=true)
	@Modifying
	public void updateByFixedAreaId(String fixedAreaId);

	
	@Query(value="update t_customer set C_FIXED_AREA_ID = ? where c_id = ?",nativeQuery=true)
	@Modifying
	public void updateFixedAreaIdById(String fixedAreaId,Integer customerId);

	/**
	 * 激活账号
	 * @param telephone
	 */
	@Query(value="update t_customer set c_type = 1 where c_telephone = ?",nativeQuery=true)
	@Modifying
	public void updateByTelephone(String telephone);

	/**
	 * 根据手机号码查询用户数据
	 * @param telephone
	 * @return
	 */
	public Customer findByTelephone(String telephone);

	/**
	 * 登录（根据用户名和密码）
	 * @param telephone
	 * @param password
	 * @return
	 */
	public Customer findByTelephoneAndPassword(String telephone, String password);

	/**
	 * 根据详细地址查询定区id
	 * @param address
	 * @return
	 */
	@Query(value="select t.c_fixed_area_id from t_customer t where t.c_address = ?",nativeQuery=true)
	public String findFixedAreaIdByAddress(String address);
	/**
	 * 根据定去 获得客户信息
	 * @param fids
	 * @return
	 */
  @Query(value="select * from T_CUSTOMER t where c_fixed_area_id =?",nativeQuery=true)
   @Modifying
	public List<Customer> findFixedAreaIdByCustomer(String fids);

}
