package cn.itheima.bos.service.base.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.ISubareaDao;
import cn.itheima.bos.domain.base.Area;
import cn.itheima.bos.domain.base.SubArea;
import cn.itheima.bos.domain.column.ColumnData;
import cn.itheima.bos.domain.column.Data;
import cn.itheima.bos.service.base.ISubareaService;
import cn.itheima.bos.utils.FileUtils;

/**
 * 业务逻辑处理层 分区
 * 
 * @author admin
 *
 */
@Transactional
@Service
public class SubareaServiceImpl implements ISubareaService {

	@Resource
	private ISubareaDao subareaDao;

	@Override
	public void save(SubArea model) {
		//设置uuid 为主键id
		//model.setId(UUID.randomUUID().toString());
		String id = model.getId();
		if(id == null || "id".equals(id) || "".equals(id)){
			model.setId(UUID.randomUUID().toString());
		}
		subareaDao.save(model);
	}

	@Override
	public void exportXls() {
		// 定义成功或错误的返回信息
		/*
		 * Map<String,String> map = new HashMap<String,String>();
		 * //map.put("success", "执行成功"); List<String> list = new ArrayList<>();
		 * map.put("error","第一行第一列"); list.add(map.toString());
		 */

		/*
		 * Map<String,String> map = new HashMap<String,String>(); List<String>
		 * list = new ArrayList<>(); list.add("第一行第一列1<br/>");
		 * list.add("第一行第一列2<br/>"); list.add("第一行第一列3<br/>");
		 * list.add("第一行第一列4<br/>"); map.put("error", list.toString());
		 */

		// 异常 ：业务异常 运行时异常
		//
		try {
			// 查询分区所有数据
			List<SubArea> listSubArea = subareaDao.findAll();

			// 创建空的excel
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = hwb.createSheet();// 创建一个新的sheet页
			// 创建标题行
			HSSFRow titleRow = sheet.createRow(0);// 创建标题第一行
			titleRow.createCell(0).setCellValue("编号");// 创建第一行第一列 设置标题
			titleRow.createCell(1).setCellValue("起始号");
			titleRow.createCell(2).setCellValue("终止号");
			titleRow.createCell(3).setCellValue("关键字");
			titleRow.createCell(4).setCellValue("辅助关键字");
			titleRow.createCell(5).setCellValue("省市区");
			// 创建数据行
			if (listSubArea != null && listSubArea.size() > 0) {
				// 创建需要导出excel文件
				for (SubArea subArea : listSubArea) {
					int lastRowNum = sheet.getLastRowNum();// 最后一行行号 0
					HSSFRow dataRow = sheet.createRow(lastRowNum + 1);// 循环创建数据行
					dataRow.createCell(0).setCellValue(subArea.getId());// 创建第一行第一列
																		// 设置标题
					dataRow.createCell(1).setCellValue(subArea.getStartNum());
					dataRow.createCell(2).setCellValue(subArea.getEndNum());
					dataRow.createCell(3).setCellValue(subArea.getKeyWords());
					dataRow.createCell(4).setCellValue(subArea.getAssistKeyWords());
					String myPCD = "";// 省市区
					Area area = subArea.getArea();
					if (area != null) {
						myPCD = area.getName();
					}
					dataRow.createCell(5).setCellValue(myPCD);
				}
			}
			String myFileName = "分区数据.xls";
			// 从request中获得哪个浏览器
			String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");

			// 文件名称 哪个浏览器
			String newFileName = FileUtils.encodeDownloadFilename(myFileName, userAgent);

			// 文件下载 一个流 两个头
			// 文件名称问题
			ServletActionContext.getResponse().setHeader("content-disposition", "filename=" + newFileName);// 设置下载的文件名称
			// 设置excel 2003文件类型 从tomcat web.xml中搜索xls //文件后缀
			ServletActionContext.getResponse().setContentType("application/vnd.ms-excel;charset=UTF-8");
			// 以输出流形式下载到本地
			ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
			hwb.write(outputStream);
			hwb.close();// 关闭流
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Object[]> doPie() {

		return subareaDao.doPie();
	}

	@Override
	public List<Data> doColumnLevelOne() {
		List<Data> dataList = new ArrayList<Data>();
		List<Object[]> list = subareaDao.doColumnLevelOne();
		for (Object[] objects : list) {
			Data data = new Data();
			data.setName(String.valueOf(objects[0]));
			data.setY((BigDecimal) objects[1]);
			data.setDrilldown(String.valueOf(objects[0]));
			dataList.add(data);
		}

		return dataList;
	}

	@Override
	public List<ColumnData> doColumnLevelTwo() {
		List<ColumnData> dataList = new ArrayList<ColumnData>();
		List<Object[]> list = subareaDao.doColumnLevelTwo();
		for (Object[] objects : list) {

			int flag = 0;
			ColumnData columnData = new ColumnData();
			columnData.setId(String.valueOf(objects[0]));
			columnData.setName("city");
			for (ColumnData columnData01 : dataList) {
				if (columnData.getId().equals(columnData01.getId())
						&& columnData.getName().equals(columnData01.getName())) {
					Data data = new Data();
					data.setName(String.valueOf(objects[1]));
					data.setY((BigDecimal) objects[2]);
					data.setDrilldown(String.valueOf(objects[1]));
					columnData01.getData().add(data);
					flag = 1;
					break;
				}
			}
			if (flag != 1) {
				Data data = new Data();
				data.setName(String.valueOf(objects[1]));
				data.setY((BigDecimal) objects[2]);
				data.setDrilldown(String.valueOf(objects[1]));
				List<Data> templist = new ArrayList<Data>();
				templist.add(data);
				columnData.setData(templist);
				dataList.add(columnData);
			}
		}

		List<Object[]> list02 = subareaDao.doColumnLevelThree();
		for (Object[] objects : list02) {
			int flag = 0;
			ColumnData columnData = new ColumnData();
			columnData.setId(String.valueOf(objects[0]));
			columnData.setName("district");
			for (ColumnData columnData01 : dataList) {
				if (columnData.getId().equals(columnData01.getId())
						&& columnData.getName().equals(columnData01.getName())) {
					Data data = new Data();
					data.setName(String.valueOf(objects[1]));
					data.setY((BigDecimal) objects[2]);
					data.setDrilldown(String.valueOf(objects[1]));
					columnData01.getData().add(data);
					flag = 1;
					break;
				}
			}
			if (flag != 1) {
				Data data = new Data();
				data.setName(String.valueOf(objects[1]));
				data.setY((BigDecimal) objects[2]);
				data.setDrilldown(String.valueOf(objects[1]));
				List<Data> templist = new ArrayList<Data>();
				templist.add(data);
				columnData.setData(templist);
				dataList.add(columnData);
			}
		}
		return dataList;
	}

	@Override
	public Page<SubArea> pageQuery(Pageable pageable) {
		
		return subareaDao.findAll(pageable);
	}

	@Override
	public List<SubArea> findSubareaByFixedAreaId(String id) {
		return subareaDao.findSubareaByFixedAreaId(id);
	}
}
