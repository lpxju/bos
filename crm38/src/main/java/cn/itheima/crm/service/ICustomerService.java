package cn.itheima.crm.service;

import java.util.List;

import cn.itheima.crm.domain.Customer;

public interface ICustomerService {
	/**
	 * 查询所有客户数据
	 * @return
	 */
	public List<Customer> findAll();
	
	//左侧：查询未关联定区的客户数据
	public List<Customer> findByFixedAreaIdIsNull();
	
	//右侧：查询已经关联定区的客户数据
	public List<Customer> findByFixedAreaId(String fixedAreaId);
	//定区id 右侧下拉框的数据客户ids
	//public void assignCustomers2FixedArea(String fixedAreaId,List<Integer> customerIds);
	/**
	 * 返回码 
	 * 000000:成功
	 * 000001：参数异常
	 * 000002：xxxx
	 * 999999：系统异常
	 * @param fixedAreaId
	 * @param customerIds
	 * @return
	 */
	public String assignCustomers2FixedArea(String fixedAreaId,List<Integer> customerIds);
	
	
	/**
	 * 注册用户
	 * @param customer
	 */
	public void regist(Customer customer);
	
	/**
	 * 激活账号
	 * @param telephone
	 */
	public void activeMail(String telephone);
	
	/**
	 * 根据手机号码查询客户信息
	 * @param telephone
	 * @return
	 */
	public Customer findByTelephone(String telephone);
	
	/**
	 * 登录（根据用户名和密码查询用户是否存在）
	 * @param telephone
	 * @param password
	 * @return
	 */
	public Customer findByTelephoneAndPassword(String telephone,String password);
	
	/**
	 * 根据发件人详细地址查询定区id
	 * @param address
	 * @return
	 */
	public String findFixedAreaIdByAddress(String address);
	
	
	/**
	 * 根据定去模块获得客户信息
	 * @param fids
	 * @return
	 */
	public List<Customer> findFixedAreaIdByCustomer(String fids);
}
