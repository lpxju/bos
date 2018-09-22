package cn.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.IAreaDao;
import cn.itheima.bos.dao.base.IFixedAreaDao;
import cn.itheima.bos.dao.base.ISubareaDao;
import cn.itheima.bos.dao.take_delivery.IOrderDao;
import cn.itheima.bos.dao.take_delivery.IWorkBillDao;
import cn.itheima.bos.domain.base.Area;
import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.domain.base.FixedArea;
import cn.itheima.bos.domain.base.SubArea;
import cn.itheima.bos.domain.take_delivery.Order;
import cn.itheima.bos.domain.take_delivery.WorkBill;
import cn.itheima.bos.service.take_delivery.IOrderService;
import cn.itheima.crm.service.CustomerServiceImpl;
/**
 * 业务逻辑处理层  在线下单实体类
 * @author admin
 *
 */
@Transactional
@Service
@WebService
public class OrderServiceImpl implements IOrderService {
	private Log log = LogFactory.getLog(OrderServiceImpl.class);

	@Resource
	private CustomerServiceImpl crmProxy;///crm服务
	
	@Resource
	private IFixedAreaDao fixedAreaDao;//定区dao
	
	@Resource
	private IOrderDao orderDao;//订单dao
	
	@Resource
	private IWorkBillDao workBillDao;//工单dao
	
	
	@Resource
	private IAreaDao areaDao;//区域dao
	
	
	@Resource
	private ISubareaDao subareaDao;//分区dao
	
	
	/*@Override
	public void save(Order order) {
		
		Area recArea = order.getRecArea();//托管对象  收件人对象重新查询
		String province = recArea.getProvince();
		String city = recArea.getCity();
		String district = recArea.getDistrict();
		Area recArea2 = areaDao.findByProvinceAndCityAndDistrict(province,city,district);
		order.setRecArea(recArea2);//重新为省市区赋值
		
		Area sendArea = order.getSendArea();//托管对象  发件人对象重新查询
		String province2 = sendArea.getProvince();
		String city2 = sendArea.getCity();
		String district2 = sendArea.getDistrict();
		Area sendArea2 = areaDao.findByProvinceAndCityAndDistrict(province2,city2,district2);
		order.setSendArea(sendArea2);//重新为省市区赋值
		
		System.out.println(order.toString());
		
		boolean flag = false;//默认未找到快递员 
		
		//精确匹配快递员
		//1、	在基础数据模块，实现定区关联客户，客户【地址】已经与定区【快递员】进行过关联，当下单地址与客户地址完全一致时，找到下单客户信息，找到匹配定区，找到快递员，自动分单成功
		String sendAddress = order.getSendAddress();//发件人详细地址 要根据此地址到crm用户下customer表中找定区id
		//根据详细地址到crm用户下 查询定区id
		String fixedAreaId = crmProxy.findFixedAreaIdByAddress(sendAddress);
		//定区不为空
		if(StringUtils.isNotBlank(fixedAreaId)){
			//根据定区id 找快递员
			//首先根据定区id查询定区对象
			FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);
			if(fixedArea != null){
				Set<Courier> couriers = fixedArea.getCouriers();//根据定区持久对象 获取快递员对象（多个快递员）
				//判断当前快递员上班时间 跟订单时间是否一致 （逻辑省略）
				if(couriers != null && couriers.size() > 0){
					for (Courier courier : couriers) {
						//只要能够找到快递员就跳出循环
						//并生成工单表
						WorkBill workBill = new WorkBill();
						workBill.setType("新");//1 2 3 工单类型
						workBill.setPickstate("待取件");//取件状态
						workBill.setBuildtime(new Date());
						workBill.setAttachbilltimes(1);//追单次数
						workBill.setRemark(order.getRemark());//备注
						workBill.setSmsNumber("12345667");///短信序号
						workBill.setCourier(courier);
						workBill.setOrder(order);
						workBillDao.save(workBill);
						//订单关联 工单 
						//order.setWorkBills(workBills);
						order.setCourier(courier);//自动分单成功才会关联
						//发送短信 伪代码
						log.info("发送短信给快递员成功：将用户地址和手机号码发送给快递员");
						flag =true;//标识自动分单成功
						break;
					}
				}
				
			}
		}
		
		
		//模糊匹配快递员
		//2、	如果下单地址没有与CRM客户地址完全一致，从下单信息中省、市、区匹配区域信息，分区属于区域，获取区域中所有分区信息，获取分区关键字、辅助关键字，下单地址匹配分区关键字/辅助关键字，找到分区，通过分区找到定区，找到匹配快递员，自动分单成功
		if(!flag){//精确匹配自动分单失败 走 模糊匹配流程
			if(sendArea2 != null){
				String id = sendArea2.getId();///区域id
				//根据区域id查询所有分区数据
				List<SubArea> list = subareaDao.findSubareaById(id);
				if(list != null && list.size() > 0){
					for (SubArea subArea : list) {
						String assistKeyWords = subArea.getAssistKeyWords();///辅助关键字
						String keyWords = subArea.getKeyWords();//关键字
						if(StringUtils.isNotBlank(sendAddress) && (sendAddress.contains(assistKeyWords) || sendAddress.contains(keyWords))){
							//如果包含分区的关键字 说明属于当前分区（小区）
							FixedArea fixedArea = subArea.getFixedArea();
							if(fixedArea != null){
								Set<Courier> couriers = fixedArea.getCouriers();//根据定区持久对象 获取快递员对象（多个快递员）
								//判断当前快递员上班时间 跟订单时间是否一致 （逻辑省略）
								if(couriers != null && couriers.size() > 0){
									for (Courier courier : couriers) {
										//只要能够找到快递员就跳出循环
										//并生成工单表
										WorkBill workBill = new WorkBill();
										workBill.setType("新");//1 2 3 工单类型
										workBill.setPickstate("待取件");//取件状态
										workBill.setBuildtime(new Date());
										workBill.setAttachbilltimes(1);//追单次数
										workBill.setRemark(order.getRemark());//备注
										workBill.setSmsNumber("12345667");///短信序号
										workBill.setCourier(courier);
										workBill.setOrder(order);
										workBillDao.save(workBill);
										//订单关联 工单 
										//order.setWorkBills(workBills);
										order.setCourier(courier);//自动分单成功才会关联
										//发送短信 伪代码
										log.info("发送短信给快递员成功：将用户地址和手机号码发送给快递员");
										flag =true;//标识自动分单成功
										break;
									}
								}
								
							}
						}
					}
				}
				
			}
		}
		
		
		if(flag){
			order.setOrderType("自动分单");
		}
		else
		{
			//人工处理
			///3、	上面业务逻辑都没有实现，则进入人工调度
			order.setOrderType("人工分单");
		}
		order.setStatus("待取件");
		order.setOrderTime(new Date());
		
		orderDao.save(order);//前台传入的省市区
	}*/
	
	
	@Override
	public void save(Order order) {
		
		Area recArea = order.getRecArea();//托管对象  收件人对象重新查询
		order.setRecArea(this.getArea(recArea));//重新为省市区赋值
		Area sendArea = order.getSendArea();//托管对象  发件人对象重新查询 
		Area sendArea2 =this.getArea(sendArea);
		order.setSendArea(sendArea2);//重新为省市区赋值
		System.out.println(order.toString());
		boolean flag = false;//默认未找到快递员 
		//精确匹配快递员
		//1、	在基础数据模块，实现定区关联客户，客户【地址】已经与定区【快递员】进行过关联，当下单地址与客户地址完全一致时，找到下单客户信息，找到匹配定区，找到快递员，自动分单成功
		String sendAddress = order.getSendAddress();//发件人详细地址 要根据此地址到crm用户下customer表中找定区id
		//根据详细地址到crm用户下 查询定区id
		String fixedAreaId = crmProxy.findFixedAreaIdByAddress(sendAddress);
		//定区不为空
		if(StringUtils.isNotBlank(fixedAreaId)){
			//根据定区id 找快递员
			//首先根据定区id查询定区对象
			FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);
			if(fixedArea != null){
				Set<Courier> couriers = fixedArea.getCouriers();//根据定区持久对象 获取快递员对象（多个快递员）
				//判断当前快递员上班时间 跟订单时间是否一致 （逻辑省略）
				if(couriers != null && couriers.size() > 0){
					for (Courier courier : couriers) {
						this.genWorkBillSendMsg(fixedArea, order);
						flag =true;//标识自动分单成功
						break;
					}
				}
				
			}
		}
		
		
		//模糊匹配快递员
		//2、	如果下单地址没有与CRM客户地址完全一致，从下单信息中省、市、区匹配区域信息，分区属于区域，获取区域中所有分区信息，获取分区关键字、辅助关键字，下单地址匹配分区关键字/辅助关键字，找到分区，通过分区找到定区，找到匹配快递员，自动分单成功
		if(!flag){//精确匹配自动分单失败 走 模糊匹配流程
			if(sendArea2 != null){
				String id = sendArea2.getId();///区域id
				//根据区域id查询所有分区数据
				List<SubArea> list = subareaDao.findSubareaById(id);
				if(list != null && list.size() > 0){
					for (SubArea subArea : list) {
						String assistKeyWords = subArea.getAssistKeyWords();///辅助关键字
						String keyWords = subArea.getKeyWords();//关键字
						if(StringUtils.isNotBlank(sendAddress) && (sendAddress.contains(assistKeyWords) || sendAddress.contains(keyWords))){
							//如果包含分区的关键字 说明属于当前分区（小区）
							FixedArea fixedArea = subArea.getFixedArea();
							//////
							this.genWorkBillSendMsg(fixedArea, order);
							flag =true;//标识自动分单成功
							break;
						}
					}
				}
				
			}
		}
		
		if(flag){
			order.setOrderType("自动分单");
		}
		else
		{
			//人工处理
			///3、	上面业务逻辑都没有实现，则进入人工调度
			order.setOrderType("人工分单");
		}
		order.setStatus("待取件");
		order.setOrderTime(new Date());
		
		orderDao.save(order);//前台传入的省市区
	}
	
	/**
	 * 根据区域托管对象 获取持久对象
	 * @param recArea
	 * @return
	 */
	public Area getArea(Area recArea){
		String province = recArea.getProvince();
		String city = recArea.getCity();
		String district = recArea.getDistrict();
		Area recArea2 = areaDao.findByProvinceAndCityAndDistrict(province,city,district);
		return recArea2;
	}
	
	
	/**
	 * 生成工单和发送短信
	 */
	
	public void  genWorkBillSendMsg(FixedArea fixedArea,Order order){
		if(fixedArea != null){
			Set<Courier> couriers = fixedArea.getCouriers();//根据定区持久对象 获取快递员对象（多个快递员）
			//判断当前快递员上班时间 跟订单时间是否一致 （逻辑省略）
			if(couriers != null && couriers.size() > 0){
				for (Courier courier : couriers) {
					//只要能够找到快递员就跳出循环
					//并生成工单表
					WorkBill workBill = new WorkBill();
					workBill.setType("新");//1 2 3 工单类型
					workBill.setPickstate("待取件");//取件状态
					workBill.setBuildtime(new Date());
					workBill.setAttachbilltimes(1);//追单次数
					workBill.setRemark(order.getRemark());//备注
					workBill.setSmsNumber("12345667");///短信序号
					workBill.setCourier(courier);
					workBill.setOrder(order);
					workBillDao.save(workBill);
					//订单关联 工单 
					//order.setWorkBills(workBills);
					order.setCourier(courier);//自动分单成功才会关联
					//发送短信 伪代码
					log.info("发送短信给快递员成功：将用户地址和手机号码发送给快递员");
					
				}
			}
			
		}
	}

}
