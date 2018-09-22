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

import cn.itheima.bos.domain.base.TakeTime;
import cn.itheima.bos.domain.base.TakeTime;
import cn.itheima.bos.service.base.ITakeTimeService;
import cn.itheima.bos.web.action.common.CommonAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 控制层 收派时间
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class TakeTimeAction extends CommonAction<TakeTime> {
 
	@Resource
	private ITakeTimeService takeTimeService;
	//查询所有取派数据
	@Action(value="takeTimeAction_listajax")
	public String listajax() throws IOException{
		List<TakeTime> list = takeTimeService.listajax();
		this.list2Json(list, new String[]{});
		return NONE;
	}
	
}
