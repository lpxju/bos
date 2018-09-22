package cn.itheima.bos.web.action.base;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itheima.bos.domain.base.Area;
import cn.itheima.bos.service.base.IAreaService;
import cn.itheima.bos.utils.FileUtils;
import cn.itheima.bos.web.action.common.CommonAction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
/**
 * 控制层 区域
 * @author admin
 *
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends CommonAction<Area> {
	
	private String q;

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

	/*
	private Area area = new Area();
	
	@Override
	public Area getModel() {
		return area;
	}
	*/
	@Resource
	private IAreaService areaService;
	private File areaFile;//文件属性
	public void setAreaFile(File areaFile) {
		this.areaFile = areaFile;
	}
	
	
	//分页查询
	/*@Action(value="areaAction_pageQuery")
	public String pageQuery() throws IOException{
		//Pageable中page：当页页码从0开始  size:每页显示的记录数 
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Area> pageArea = areaService.pageQuery(pageable);
		///将page<T>数据转json数据 返回前台页面展示 到datagrid中
		long total = pageArea.getTotalElements();//总记录数
		List<Area> listRows = pageArea.getContent();///当前页码数据 list
		//将total和rows放入map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		map.put("rows", listRows);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"subareas"});
		String json = JSONObject.fromObject(map,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(json);
		return NONE;
	}*/
	
	public void setQ(String q) {
		this.q = q;
	}


	@Action(value="areaAction_pageQuery")
	public String pageQuery() throws IOException{
		//Pageable中page：当页页码从0开始  size:每页显示的记录数 
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Area> pageArea = areaService.pageQuery(pageable);
		this.page2Json(pageArea, new String[]{"subareas"});
		return NONE;
	}
	
	
	//查询所有取派数据
	@Action(value="areaAction_importXls")
	public String importXls() throws IOException{
		//输入参数 输出返回结果 业务代码写到service层
		String rs = areaService.importXls(areaFile);
		if(rs.equals("00000")){
			///如果成功 可以给用户提示
		}
		return NONE;
	}
	
	
	///区域数据查询
	@Action(value="areaAction_findAll")
	public String findAll() throws IOException{
		List<Area> listArea = null;
		//如果不为空 则根据q模糊查询
		if(StringUtils.isNotBlank(q)){
			//前台输入的  以及 后台数据库的数据 全部转成大写进行对比
			listArea = areaService.findByQ("%"+q.toUpperCase()+"%");
		}
		else
		{
			//查询所有区域数据
			listArea = areaService.findAll();
		}
		
		
		this.list2Json(listArea, new String[]{"subareas"});
		return NONE;
	}
	
	@Resource
	DataSource dataSource;
	
	//导出PDF
	@Action(value="areaAction_exportPDF")
	public String exportPDF() throws IOException, JRException, SQLException{
		// 读取 jrxml 文件
		String jrxml = ServletActionContext.getServletContext().getRealPath("/jasper/area.jrxml");
		// 准备需要数据
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("company", "传智播客");
		// 准备需要数据  将jrxml模板 编译 报表对象
		JasperReport report = JasperCompileManager.compileReport(jrxml);
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());

		HttpServletResponse response = ServletActionContext.getResponse();
		OutputStream ouputStream = response.getOutputStream();
		// 设置相应参数，以附件形式保存PDF
		response.setContentType("application/pdf");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + FileUtils.encodeDownloadFilename("工作单.pdf",
				ServletActionContext.getRequest().getHeader("user-agent")));
		// 使用JRPdfExproter导出器导出pdf
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
		exporter.exportReport();// 导出
		ouputStream.close();// 关闭流
		
		return NONE;
	}
	
}
