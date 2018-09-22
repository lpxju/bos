package cn.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.domain.base.FixedArea;
import cn.itheima.bos.domain.base.SubArea;
import cn.itheima.bos.service.base.IFixedAreaService;
import cn.itheima.bos.service.base.ISubareaService;
import cn.itheima.bos.utils.FileUtils;
import cn.itheima.bos.web.action.common.CommonAction;
import cn.itheima.crm.service.Customer;
import cn.itheima.crm.service.CustomerServiceImpl;
/**
 * 控制层 定区
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea> {
	///日志对象
	private Log log = LogFactory.getLog(FixedAreaAction.class);
	
	//定义list集合来接收 客户ids
	private List<Integer> customerIds; 
	private Integer courierId;
	private Integer takeTimeId;
	public void setCustomerIds(List<Integer> customerIds) {
		this.customerIds = customerIds;
	}
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}
	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	@Resource
	private IFixedAreaService fixedAreaService;
	//保存
	@Action(value="fixedAreaAction_save",results={
			@Result(location="/pages/base/fixed_area.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect")
	})
	public String save(){
		fixedAreaService.save(getModel());
		return SUCCESS;
	}
	//分页
	@Action(value="fixedAreaAction_pageQuery")
	public String pageQuery() throws IOException{
		Pageable pageable = new PageRequest(page-1, rows);
		Page<FixedArea> pageFixedArea = fixedAreaService.pageQuery(pageable);
		this.page2Json(pageFixedArea, new String[]{"subareas","couriers"});
		return NONE;
	}
	
	//注入crm服务
	@Resource
	private CustomerServiceImpl customerProxy;
	
	//扩展两个方法
	//左侧：查询未关联定区的客户数据 findByFixedAreaIdIsNull
	@Action(value="fixedAreaAction_findByFixedAreaIdIsNull")
	public String findByFixedAreaIdIsNull() throws IOException{
		List<Customer> list = customerProxy.findByFixedAreaIdIsNull();
		this.list2Json(list, new String[]{});
		return NONE;
	}
	//右侧：查询已经关联定区的客户数据findByFixedAreaId
	@Action(value="fixedAreaAction_findByFixedAreaId")
	public String findByFixedAreaId() throws IOException{
		List<Customer> list = customerProxy.findByFixedAreaId(getModel().getId());
		this.list2Json(list, new String[]{});
		return NONE;
	}
	//fixedAreaAction_assignCustomers2FixedArea 定区关联客户
	@Action(value="fixedAreaAction_assignCustomers2FixedArea",results={
			@Result(location="/pages/base/fixed_area.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect")
	})
	public String assignCustomers2FixedArea() throws IOException{
		//输入  页面请求的参数
		log.info("定区id"+getModel().getId()+";客户ids:"+customerIds.toString());
		log.debug("debug");
		log.error("error");
		log.fatal("fatal");
		log.trace("trace");
		log.warn("warn");
		//fatal error warn info debug trace
		String rs = customerProxy.assignCustomers2FixedArea(getModel().getId(), customerIds);
		//输出 返回的结果
		log.info("返回值"+rs);
		if(StringUtils.isNotBlank(rs) && rs.equals("000000")){
			return SUCCESS;
		}
		return "fail";
	}
	
	
	//associationCourierToFixedArea定区关联快递员
	@Action(value="fixedAreaAction_associationCourierToFixedArea",results={
			@Result(location="/pages/base/fixed_area.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect")
	})
	public String associationCourierToFixedArea() throws IOException{
		fixedAreaService.associationCourierToFixedArea(getModel().getId(),courierId,takeTimeId);
		return SUCCESS;
	}
	
	private String fids;
	public void setFids(String fids) {
		this.fids = fids;
	}
	////---------------------------
	@Action(value = "fixedAreaAction_exportXls")
	public String exportXls() throws IOException {
		String[] split = fids.split(",");
		// 2.创建excel对象
		HSSFWorkbook hs = new HSSFWorkbook();
		// 3.创建sheet页
		HSSFSheet createSheet = hs.createSheet("客户信息表");
		// 4.在sheet页中创建标题行
		HSSFRow titleRow = createSheet.createRow(0);
		// 5.创建标题行内人
		titleRow.createCell(0).setCellValue("编号");
		titleRow.createCell(1).setCellValue("用户名");
		titleRow.createCell(2).setCellValue("密码");
		titleRow.createCell(3).setCellValue("类型");
		titleRow.createCell(4).setCellValue("性别");
		titleRow.createCell(5).setCellValue("生日");
		titleRow.createCell(6).setCellValue("电话");
		titleRow.createCell(7).setCellValue("公司");
		titleRow.createCell(8).setCellValue("部门");
		titleRow.createCell(9).setCellValue("职位");
		titleRow.createCell(10).setCellValue("地址");
		titleRow.createCell(11).setCellValue("邮箱");
		for (String fid : split) {
			List<Customer> list = customerProxy.findFixedAreaIdByCustomer(fid);
			System.out.println("List<Customer> list =" + list);
			if (list == null) {
				continue;
			}
			for (Customer customer : list) {
				String bridays = customer.getBirthday();
				System.out.println(bridays);
				HSSFRow dataRow = createSheet.createRow(createSheet.getLastRowNum() + 1);
				dataRow.createCell(0).setCellValue(customer.getId());
				dataRow.createCell(1).setCellValue(customer.getUsername());
				dataRow.createCell(2).setCellValue(customer.getPassword());
				dataRow.createCell(3).setCellValue(customer.getType());
				dataRow.createCell(4).setCellValue(customer.getSex());
				dataRow.createCell(5).setCellValue(bridays);
				dataRow.createCell(6).setCellValue(customer.getTelephone());
				dataRow.createCell(7).setCellValue(customer.getCompany());
				dataRow.createCell(8).setCellValue(customer.getDepartment());
				dataRow.createCell(9).setCellValue(customer.getPosition());
				dataRow.createCell(10).setCellValue(customer.getAddress());
				dataRow.createCell(11).setCellValue(customer.getEmail());
			}
		}
		String filename = "客户表数据.xls";
		String agent = (String) ServletActionContext.getRequest().getHeader("User-Agent");
		String newFileName = FileUtils.encodeDownloadFilename(filename, agent);
		// 通过输出流将excel数据写回浏览器
		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
		ServletActionContext.getResponse().setContentType("application/vnd.ms-excel;charset=UTF-8");
		ServletActionContext.getResponse().setHeader("content-disposition", "filename=" + newFileName);
		hs.write(outputStream);
		hs.close();
		return NONE;
	}
	
	//查询关联快递员:fixedAreaAction_findCourierByFixedAreaId
		@Action(value="fixedAreaAction_findCourierByFixedAreaId")
		public String findCourierByFixedAreaId() throws IOException{
			String id = getModel().getId();
			FixedArea fixedArea = fixedAreaService.findCourierByFixedAreaId(id);
			Set<Courier> couriers = fixedArea.getCouriers();
			List<Courier> list = new ArrayList<Courier>();
			for (Courier courier : couriers) {
				list.add(courier);
			}
			if(list != null && list.size() > 0){
				this.list2Json(list, new String[]{"fixedAreas"});
			}
			return NONE;
		}
		
		
		//查询关联分区:fixedAreaAction_findSubareaByFixedAreaId
		@Resource
		private ISubareaService subareaService;
		@Action(value="fixedAreaAction_findSubareaByFixedAreaId")
		public String findSubareaByFixedAreaId() throws IOException{
			String id = getModel().getId();
			List<SubArea> list = subareaService.findSubareaByFixedAreaId(id);
			this.list2Json(list, new String[]{"fixedArea","subareas"});
			return NONE;
		}
}
