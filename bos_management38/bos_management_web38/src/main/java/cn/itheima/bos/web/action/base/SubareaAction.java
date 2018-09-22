package cn.itheima.bos.web.action.base;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.ht.util.Config;
import com.ht.util.FileUtil;

import cn.itheima.bos.domain.base.SubArea;
import cn.itheima.bos.domain.column.ColumnData;
import cn.itheima.bos.domain.column.Data;
import cn.itheima.bos.service.base.ISubareaService;
import cn.itheima.bos.web.action.common.CommonAction;
/**
 * 控制层 分区
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class SubareaAction extends CommonAction<SubArea> {
	
	@Autowired
	private Config config;
	 
	@Resource
	private ISubareaService subareaService;
	
	//分页
		@Action(value="subAreaAction_pageQuery")
		public String pageQuery() throws IOException{
			Pageable pageable = new PageRequest(page-1, rows);
			Page<SubArea> pagesubArea = subareaService.pageQuery(pageable);
			this.page2Json(pagesubArea, new String[]{});
			return NONE;
		}
	
	//保存
	@Action(value="subareaAction_save",results={
			@Result(location="/pages/base/sub_area.html",name="success",type="redirect"),
			@Result(location="/pages/error.html",name="fail",type="redirect")
	})
	public String save(){
		subareaService.save(getModel());
		return SUCCESS;
	}
	
	
	//导出分区所有数据 
	@Action(value="subAreaAction_exportXls")
	public String exportXls(){
		//没有输入参数 也没有返回值   以文件输出流形式下载到本地
		subareaService.exportXls();
		return NONE;
	}
	
	
	
	//分区模板下载
	@Action(value="subAreaAction_doTemplateExport")
	public String doTemplateExport(){
		//文件下载路径
		String templatePath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ config.getTemplateFolder()
				+ File.separator
				+ "subarea_template.xls";
		FileUtil.download(templatePath, ServletActionContext.getResponse());
		return NONE;
	}
	
	
	//分区分布图
	@Action(value="subareaAction_doPie")
	public String doPie() throws IOException{
		List<Object[]> list = subareaService.doPie();
		this.list2Json(list, new String[]{});
		return NONE;
	}
	
	//分区分布图column
	@Action(value="subareaAction_doColumn")
	public String doColumn() throws IOException{
		List<Data> list = subareaService.doColumnLevelOne();
		this.list2Json(list, new String[]{});
		return NONE;
	}
	
	@Action(value="subareaAction_doDrilldown")
	public String doDrilldown() throws IOException{
		List<ColumnData> list = subareaService.doColumnLevelTwo();
		this.list2Json(list, new String[]{});
		return NONE;
	}
	
}
