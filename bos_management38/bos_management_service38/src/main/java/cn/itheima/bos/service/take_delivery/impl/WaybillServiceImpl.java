package cn.itheima.bos.service.take_delivery.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.base.IAreaDao;
import cn.itheima.bos.dao.take_delivery.IOrderDao;
import cn.itheima.bos.dao.take_delivery.IWaybillDao;
import cn.itheima.bos.domain.base.Area;
import cn.itheima.bos.domain.take_delivery.Order;
import cn.itheima.bos.domain.take_delivery.WayBill;
import cn.itheima.bos.service.take_delivery.IWaybillService;
/**
 * 运单模块（业务逻辑处理层接口实现类）
 * @author admin
 *
 */
@Service
@Transactional
@WebService
public class WaybillServiceImpl implements IWaybillService {

	@Resource
	private IWaybillDao waybillDao;
	@Resource
	private IOrderDao orderDao;
	@Resource
	private IAreaDao areaDao;
	
	@Override
	public void save(WayBill model) {
		waybillDao.save(model);
	}
	
	List<String> list=null;
	@Override
	public List<String> importXls(File waybillFile) {
		XSSFWorkbook xs = null;
		list = new ArrayList<>();
		try {
			//相当于得到一个excel对象
			xs = new XSSFWorkbook(new FileInputStream(waybillFile));
			//获得一个Sheet对象 
			XSSFSheet sheet = xs.getSheetAt(0);
			//定一个list集合存放每一行数据
			List<WayBill> listWayBill = new ArrayList<WayBill>();
			//获取每一行数据
			list = checkSheet(sheet);
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				String wayBillNum = row.getCell(0).getStringCellValue();
				if (!"".equals(wayBillNum)) {
					WayBill wayBill = waybillDao.findByWayBillNum(wayBillNum);
					if (wayBill!=null) {
						list.add(createMsg(row, 0));
					}
				}
				double orderDouble = Double.parseDouble(row.getCell(1).getStringCellValue());
				int orderId = (int) orderDouble;
				Order order = orderDao.findOne(orderId);
				if (order==null) {
					list.add(createMsg(row, 1));
				}
				String sendName = row.getCell(2).getStringCellValue();
				String sendMobile = row.getCell(3).getStringCellValue();
				String sendCompany = row.getCell(4).getStringCellValue();
				String sendAreaStr = row.getCell(5).getStringCellValue();
				Area sendArea =null;
				if (!"".equals(sendAreaStr)) {
					String[] split = sendAreaStr.split("-");
					if (split.length<3) {
						list.add(createMsg(row, 5));
					}else {
						sendArea = areaDao.findByProvinceAndCityAndDistrict(split[0],split[1],split[2]);
						if (sendArea==null) {
							list.add(createMsg(row, 5));
						}
					}
				}
				String sendAddress = row.getCell(6).getStringCellValue();
				String recName = row.getCell(7).getStringCellValue();
				String recMobile = row.getCell(8).getStringCellValue();
				String recCompany = row.getCell(9).getStringCellValue();
				String recAreaStr = row.getCell(10).getStringCellValue();
				Area recArea =null;
				if (!"".equals(recAreaStr)) {
					String[] split = recAreaStr.split("-");
					if (split.length<3) {
						list.add(createMsg(row, 10));
					}else {
						recArea = areaDao.findByProvinceAndCityAndDistrict(split[0],split[1],split[2]);
						if (recArea==null) {
							list.add(createMsg(row, 10));
						}
					}
				}
				String recAddress = row.getCell(11).getStringCellValue();
				String sendProNum = row.getCell(12).getStringCellValue();
				String goodsType = row.getCell(13).getStringCellValue();
				String payTypeNum = row.getCell(14).getStringCellValue();
				Double weight = Double.parseDouble(row.getCell(15).getStringCellValue());
				String remark = row.getCell(16).getStringCellValue();
				double numDouble = Double.parseDouble(row.getCell(17).getStringCellValue());
				Integer num = (int) numDouble;
				String arriveCity = row.getCell(18).getStringCellValue();
				double feeitemnumDouble = Double.parseDouble(row.getCell(19).getStringCellValue());
				Integer feeitemnum = (int) feeitemnumDouble;
				Double actlweit = Double.parseDouble(row.getCell(20).getStringCellValue());
				String vol = row.getCell(21).getStringCellValue();
				String floadreqr = row.getCell(22).getStringCellValue();
				String wayBillType = row.getCell(23).getStringCellValue();
				Integer signStatus = (int) Double.parseDouble(row.getCell(24).getStringCellValue());
				String delTag = row.getCell(25).getStringCellValue();
				//得到row这每一行数据 放入area实体对象
				//WayBill wayBill = new WayBill();
				WayBill wayBill = new WayBill(null, wayBillNum, order, sendName, sendMobile,
						sendCompany, sendArea, sendAddress, recName, recMobile,
						recCompany, recArea, recAddress, sendProNum, goodsType,
						payTypeNum, weight, remark, num, arriveCity,
						feeitemnum, actlweit, vol, floadreqr, wayBillType,
						signStatus, delTag);
				listWayBill.add(wayBill);
			}
			//先查询 如果数据存在则更新 
			//如果数据不存在则插入
			if (list.size()==0) {
				waybillDao.save(listWayBill);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list.add("(00001)服务器异常,请联系管理员!");
		}finally {
			try {
				xs.close();
			} catch (IOException e) {
				e.printStackTrace();
				list.add("(00002)方法区内存异常,请联系管理员!");
			}//关闭流
			
		}
		return list;
	}
	
	  public List<String> checkSheet(XSSFSheet sheet){
		  list=new ArrayList<>();
		  int lastRowNum = sheet.getLastRowNum();
		  if (lastRowNum<=0) {
			list.add("表格内无数据,请确认后重新提交!");
		}else {
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
				int lastCellNum = sheet.getRow(0).getLastCellNum();
				for(int i=0;i<=lastCellNum-1;i++){
					Cell cell = row.getCell(i);
					if (cell==null) {
						list.add(createMsg(row, i));
						continue;
					}
			        int cellType = cell.getCellType();
			        if(cellType == Cell.CELL_TYPE_NUMERIC){
		                cell.setCellType(Cell.CELL_TYPE_STRING);
		            }
			        if (cellType==Cell.CELL_TYPE_STRING||cellType==Cell.CELL_TYPE_NUMERIC) {
			            continue;
			        }
					list.add(createMsg(row, i));
				}
			}
		}
		return list;  
      }
	  public String createMsg(Row row,int cellNum){
		  return "第"+row.getRowNum()+"行第"+(cellNum+1)+"列数据错误,请核对重新操作!<br/>";
	  }

	@Override
	public Page<WayBill> pageQuery(Pageable pageable) {
		return waybillDao.findAll(pageable);
	}
}
