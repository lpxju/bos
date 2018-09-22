package cn.itheima.crm.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.crm.dao.ICustomerDao;
import cn.itheima.crm.domain.Customer;
import cn.itheima.crm.service.ICustomerService;
/**
 * 客户实现类
 * @author admin
 *
 */
@WebService
@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

	@Resource
	private ICustomerDao customerDao;
	
	@Override
	public List<Customer> findAll() {
		return customerDao.findAll();
	}

	@Override
	public List<Customer> findByFixedAreaIdIsNull() {
		return customerDao.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findByFixedAreaId(String fixedAreaId) {
		return customerDao.findByFixedAreaId(fixedAreaId);
	}

	/*@Override
	public void assignCustomers2FixedArea(String fixedAreaId, List<Integer> customerIds) {
		//开发服务端一定要做参数校验 
		if(StringUtils.isNotBlank(fixedAreaId)){
			//先清空当前定区关联的客户数据(定区id)
			//update t_customer set C_FIXED_AREA_ID = '' where C_FIXED_AREA_ID = ?
			customerDao.updateByFixedAreaId(fixedAreaId);
			
			if(customerIds !=null &&customerIds.size() >0){
				for (Integer customerId : customerIds) {
					//重新建立定区和客户的关联 （定区id  客户ids)
					//updateFixedAreaIdById根据客户id来关联定区
					customerDao.updateFixedAreaIdById(fixedAreaId,customerId);
				}
			}
		}
		
	}*/
	
	@Override
	public String assignCustomers2FixedArea(String fixedAreaId, List<Integer> customerIds) {
		//开发服务端一定要做参数校验 
		if(StringUtils.isNotBlank(fixedAreaId)){
			//先清空当前定区关联的客户数据(定区id)
			//update t_customer set C_FIXED_AREA_ID = '' where C_FIXED_AREA_ID = ?
			try {
				customerDao.updateByFixedAreaId(fixedAreaId);
				
				if(customerIds !=null &&customerIds.size() >0){
					for (Integer customerId : customerIds) {
						//重新建立定区和客户的关联 （定区id  客户ids)
						//updateFixedAreaIdById根据客户id来关联定区
						customerDao.updateFixedAreaIdById(fixedAreaId,customerId);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "999999";
			}
			return "000000";
		}
		return "000001";//Constants.ONE
		
	}

	@Override
	public void regist(Customer customer) {
		customerDao.save(customer);
	}

	@Override
	public void activeMail(String telephone) {
		customerDao.updateByTelephone(telephone);
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerDao.findByTelephone(telephone);
	}

	@Override
	public Customer findByTelephoneAndPassword(String telephone, String password) {
		return customerDao.findByTelephoneAndPassword(telephone,password);
	}

	@Override
	public String findFixedAreaIdByAddress(String address) {
		return customerDao.findFixedAreaIdByAddress(address);
	}

	@Override
	public List<Customer> findFixedAreaIdByCustomer(String fids) {
		// TODO Auto-generated method stub
		return customerDao.findFixedAreaIdByCustomer(fids);
	}

}
