package cn.itheima.bos.web.action.take_delivery;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.ht.util.Config;
import com.ht.util.FileUtil;

import cn.itheima.bos.domain.take_delivery.WayBill;
import cn.itheima.bos.service.take_delivery.IWaybillService;
import cn.itheima.bos.web.action.common.CommonAction;
/**
 * 运单模块（展示层）
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class WaybillAction extends CommonAction<WayBill> {
	
	@Autowired
	private Config config;
	
	@Resource
	private IWaybillService waybillService;
	
	private File waybillFile;//文件属性
	public void setWaybillFile(File waybillFile) {
		this.waybillFile = waybillFile;
	}
	
	@Action(value="waybillAction_save")
	public String save() throws IOException{
		String rs = "1";
		try {
			waybillService.save(getModel());
		} catch (Exception e) {
			rs = "0";
		}
		//通过输出流写回页面
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(rs);
		return NONE;
	}
	
	//运单模板下载
		@Action(value="waybillAction_doTemplateExport")
		public String doTemplateExport(){
			//文件下载路径
			String templatePath = ServletActionContext.getServletContext().getRealPath(
					"/")
					+ config.getTemplateFolder()
					+ File.separator
					+ "waybill_template.xlsx";
			FileUtil.download(templatePath, ServletActionContext.getResponse());
			return NONE;
		}
		
		//查询所有取派数据
		@Action(value="waybillAction_importXls")
		public String importXls() throws IOException{
			//输入参数 输出返回结果 业务代码写到service层
			List<String> list = waybillService.importXls(waybillFile);
			if (list!=null&&list.size()>0) {
				list2Json(list, new String[]{});
			}
			return NONE;
		}
			
		//分页查询
		@Action(value="waybillAction_pageQuery")
		public String pageQuery() throws IOException{
			Pageable pageable = new PageRequest(page-1, rows);
			Page<WayBill> pageStandard = waybillService.pageQuery(pageable);
			this.page2Json(pageStandard, new String[]{"sendArea","recArea","order"});
			return NONE;
		}
}
