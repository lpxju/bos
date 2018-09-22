package cn.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.domain.base.FixedArea;
import cn.itheima.bos.domain.base.Standard;
import cn.itheima.bos.domain.base.Courier;
import cn.itheima.bos.service.base.ICourierService;
import cn.itheima.bos.web.action.common.CommonAction;
import cn.itheima.crm.service.Customer;
import cn.itheima.crm.service.CustomerServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
/**
 * 控制层 快递员
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends CommonAction<Courier> {
	
	private String ids;//需要删除快递员ids
	//rows:每页显示的记录数    页面输入的参数
	//page:当前页码 从1开始
	/*private Integer rows;
	private Integer page;
	public void setIds(String ids) {
		this.ids = ids;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public void setPage(Integer page) {
		this.page = page;
	}*/

	/*private Courier courier = new Courier();
	
	@Override
	public Courier getModel() {
		return courier;
	}*/
	
	@Resource
	private ICourierService courierService;

	//保存
	@Action(value="courierAction_save",results={
			@Result(location="/pages/base/courier.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect")
	})
	public String save(){
		model.setDeltag('2');
		courierService.save(model);
		return SUCCESS;
	}
	
	//分页查询
	/*@Action(value="courierAction_pageQuery")
	public String pageQuery() throws IOException{
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Courier> pageCourier = courierService.pageQuery(pageable);
		long total = pageCourier.getTotalElements();//总记录数
		List<Courier> rows = pageCourier.getContent();///当前页码数据 list
		//将total和rows放入map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", rows);
		JsonConfig jsonConfig = new JsonConfig();//将不需的数据对象放入此对象中
		jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
		String json = JSONObject.fromObject(map,jsonConfig).toString();//最终需要将排除数据放入fromObject方法中
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);
		return NONE;
	}*/
	
	//@Resource
	//private CustomerServiceImpl customerProxy;
	
	@Action(value="courierAction_pageQuery")
	public String pageQuery() throws IOException{
		//调用crm服务端测试
		///List<Customer> findAll = customerProxy.findAll();
		///System.out.println(findAll.size());
		
		Pageable pageable = new PageRequest(page-1, rows);
		
		///获取页面输入的查询数据
		final String courierNum = getModel().getCourierNum();//快递员工号
		final Standard standard = getModel().getStandard();//获取收派标准名称
		final String company = getModel().getCompany();//公司
		final String type = getModel().getType();//类型
		
		//在分页的基础上拼接查询条件
		Specification<Courier> spec = new Specification<Courier>() {
			//熟悉api  Predicate:就是拼接查询条件
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// Root接口：代表Criteria查询的根对象，它与SQL查询中的FROM子句类似 from 实体类 操作实体类 来操作数据库
				//CriteriaQuery接口：代表一个specific的顶层查询对象，它包含着查询的各个部分，比如：select 、from、where、group by、order by等
				// CriteriaBuilder接口：通过调用CriteriaBuilder对象 的条件 （ equal notEqual， gt， ge，lt， le，between，like等）创建Predicate对象
				//select *from table where a like 1 and b = 2 between 1 and 2  CriteriaBuilder
				//定一个list将所有的查询条件存入
				List<Predicate> list = new ArrayList<>();
				if(StringUtils.isNotBlank(courierNum)){
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class),courierNum);//x：表达式 sql语句表达式 需要查询的属性= y:对应属性的值（页面输入的）
					list.add(p1);
				}
				if(StringUtils.isNotBlank(company)){
					Predicate p2 = cb.like(root.get("company").as(String.class),"%"+company+"%");//x：表达式 sql语句表达式 需要查询的属性= y:对应属性的值（页面输入的）
					list.add(p2);
				}
				if(StringUtils.isNotBlank(type)){
					Predicate p3 = cb.equal(root.get("type").as(String.class),type);//x：表达式 sql语句表达式 需要查询的属性= y:对应属性的值（页面输入的）
					list.add(p3);
				}
				//root.get 当前表中属性
				//root.join ==inner join
				if(standard != null &&StringUtils.isNotBlank(standard.getName())){
					///join 相当于 Standard实体类对象
					Join<Object, Object> join = root.join("standard");//inner join t_standard ts on tc.c_standard_id = ts.c_id
					Predicate p4 = cb.equal(join.get("name").as(String.class),standard.getName());
					list.add(p4);
				}
				if(list == null || list.size() == 0){
					return null;
				}
				//将list转成数组
				Predicate[] pre = new Predicate[list.size()];
				//cb:条件构造的方法 自带方法将list转Predicate对象  and每个条件之间使用and关键字连接 a=1 and b=1
				return cb.and(list.toArray(pre));//将需要转的数组类型 传入 既可转成此类型的数组数据
			}
		};//toPredicate  拼接查询条件
		Page<Courier> pageCourier = courierService.pageQuery(spec, pageable);
		/*long total = pageCourier.getTotalElements();//总记录数
		List<Courier> rows = pageCourier.getContent();///当前页码数据 list
		//将total和rows放入map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", rows);
		JsonConfig jsonConfig = new JsonConfig();//将不需的数据对象放入此对象中
		jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
		String json = JSONObject.fromObject(map,jsonConfig).toString();//最终需要将排除数据放入fromObject方法中
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);*/
		this.page2Json(pageCourier, new String[]{"subareas","couriers","takeTime"});
		return NONE;
	}
	
	//批量删除
	
	private Integer param;
	
	public void setParam(Integer param) {
		this.param = param;
	}

	@Action(value="courierAction_delete",results={
			@Result(location="/pages/base/courier.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect"),
			@Result(location="/unauthorizedUrl.html",name="unauthorizedException",type="redirect")
	})
	public String delete(){
		
		try {
			courierService.delete(ids,param);
		} catch (Exception e) {
			//没有权限的异常
			if(e instanceof UnauthorizedException){
				return "unauthorizedException";
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return SUCCESS;
	}
	
	
	/*@Action(value="courierAction_restore",results={
			@Result(location="/pages/base/courier.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect"),
			@Result(location="/unauthorizedUrl.html",name="unauthorizedException",type="redirect")
	})
	public String restore(){
		
		try {
			courierService.restore(ids);
		} catch (Exception e) {
			//没有权限的异常
			if(e instanceof UnauthorizedException){
				return "unauthorizedException";
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return SUCCESS;
	}*/
	
	///定区关联快递员 -查询快递员所有数据 （不包含作废）
	@Action(value="courierAction_listajax")
	public String listajax() throws IOException{
		List<Courier> list = courierService.listajx();
		this.list2Json(list, new String[]{"takeTime","fixedAreas"});
		return NONE;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	 
	
}
