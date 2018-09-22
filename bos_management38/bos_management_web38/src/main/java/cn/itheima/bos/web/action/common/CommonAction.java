package cn.itheima.bos.web.action.common;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itheima.bos.domain.base.Area;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {

	//定一个T模型类型对象
	protected T model;//private私有 protected子类可以使用 public都可以使用
	protected Integer rows;
	protected Integer page;
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	@Override
	public T getModel() {
		return model;
	}
	//如何为model实例化
	//通过构造方法获取父类类上数组 ，通过数组得到字一个数组值 实体对象类型 
	public CommonAction(){
		//genericSuperclass父类类型CommonAction  
		ParameterizedType genericSuperclass = (ParameterizedType)this.getClass().getGenericSuperclass();
		//获取父类类上泛型数组
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		//实体对象类型
		Class<T> entityClass = (Class<T>)actualTypeArguments[0];
		
		//通过java反射得到实体对象
		try {
			model = entityClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 将page对象转json对象 工具方法
	 * @param page
	 * @param excludes
	 * @throws IOException
	 */
	public void page2Json(Page<T> page,String[] excludes) throws IOException{
		///将page<T>数据转json数据 返回前台页面展示 到datagrid中
		long total = page.getTotalElements();//总记录数
		List<T> listRows = page.getContent();///当前页码数据 list
		//将total和rows放入map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", listRows);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		String json = JSONObject.fromObject(map,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);
	}
	/**
	 * 将list集合转json对象 工具方法
	 * @param list xxxx
	 * @param excludes xxxxx
	 * @throws IOException xxxx
	 */
	public void list2Json(List list,String[] excludes) throws IOException{
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		String json = JSONArray.fromObject(list,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);
	}

}
