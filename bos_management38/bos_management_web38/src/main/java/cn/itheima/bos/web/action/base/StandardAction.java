package cn.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itheima.bos.domain.base.Standard;
import cn.itheima.bos.service.base.IStandardService;
import cn.itheima.bos.web.action.common.CommonAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 控制层 收派标准
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class StandardAction extends CommonAction<Standard> {

	//rows:每页显示的记录数    页面输入的参数
	//page:当前页码 从1开始
	/*private Integer rows;
	private Integer page;
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public void setPage(Integer page) {
		this.page = page;
	}*/

	/*private Standard standard = new Standard();
	
	@Override
	public Standard getModel() {
		return standard;
	}*/
	
	@Resource
	private IStandardService standardService;

	//保存
	@Action(value="standardAction_save",results={
			@Result(location="/pages/base/standard.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect")
	})
	public String save(){
		standardService.save(getModel());
		return SUCCESS;
	}
	
	//分页查询
	@Action(value="standardAction_pageQuery")
	public String pageQuery() throws IOException{
		
		//Pageable中page：当页页码从0开始  size:每页显示的记录数 
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Standard> pageStandard = standardService.pageQuery(pageable);
		///将page<T>数据转json数据 返回前台页面展示 到datagrid中
		this.page2Json(pageStandard, new String[]{});
		
		/*long total = pageStandard.getTotalElements();//总记录数
		List<Standard> rows = pageStandard.getContent();///当前页码数据 list
		//将total和rows放入map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", rows);
		//datagrid数据格式
		
		{                                                      
			"total":100,	
			"rows":[ 
					{"id":"10000","name":"杨航","sal":"15000"},
					{"id":"10001","name":"余奇峰","sal":"20000"}
				   ]
		}
		//将map转json数据 JSON-lib
		//JSONObject map 对象
		//JSONArray list 数组
		String json = JSONObject.fromObject(map).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);*/
		return NONE;
	}
	
	//查询所有取派数据
	@Action(value="standardAction_findAll")
	public String findAll() throws IOException{
		List<Standard> list = standardService.findAll();
		this.list2Json(list, new String[]{});
		return NONE;
	}
	
}
