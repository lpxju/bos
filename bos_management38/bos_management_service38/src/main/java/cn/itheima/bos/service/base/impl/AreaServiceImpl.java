package cn.itheima.bos.service.base.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itheima.bos.dao.base.IAreaDao;
import cn.itheima.bos.domain.base.Area;
import cn.itheima.bos.service.base.IAreaService;
/**
 * 业务逻辑处理层  区域
 * @author admin
 *
 */
@Transactional
@Service
public class AreaServiceImpl implements IAreaService {

	
	@Resource
	private IAreaDao areaDao;
	
	@Override
	public String importXls(File areaFile){
		try {
			//相当于得到一个excel对象
			HSSFWorkbook hs = new HSSFWorkbook(new FileInputStream(areaFile));
			//获得一个Sheet对象 
			HSSFSheet sheet = hs.getSheetAt(0);
			//定一个list集合存放每一行数据
			List<Area> listArea = new ArrayList<Area>();
			//获取每一行数据
			for (Row row : sheet) {
				int rowNum = row.getRowNum();
				if(rowNum == 0){
					continue;
				}
				
				String id =row.getCell(0).getStringCellValue();//区域编号 getCell(0):得到第一个单元格  getStringCellValue:获取单元格值
				String province =row.getCell(1).getStringCellValue();//省份
				String city =row.getCell(2).getStringCellValue();//城市
				String district =row.getCell(3).getStringCellValue();//区
				String postcode =row.getCell(4).getStringCellValue();//邮编
				
				String province2 = province.substring(0, province.length()-1); 	
				String city2 =city.substring(0, city.length()-1); 	
				String district2 = district.substring(0, district.length()-1); 	
				//查找字符串首字母
				String[] headByString = PinYin4jUtils.getHeadByString(province2+city2+district2);
				//简单的将各个字符数组之间连接起来
				String shortcode = PinYin4jUtils.stringArrayToString(headByString);
				//citycode城市编码  山西省 太原市 小店区        太原市==>太原==>taiyuan
				//将汉字转换成拼音
				String citycode = PinYin4jUtils.hanziToPinyin(city2,"");
				
				//得到row这每一行数据 放入area实体对象
				Area area = new Area(id, province, city, district, postcode, citycode, shortcode, null);
				listArea.add(area);
			}
			//先查询 如果数据存在则更新 
			//如果数据不存在则插入
			areaDao.save(listArea);
			hs.close();//关闭流
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "000001";
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "000002";
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "000003";
		}
		return "000000";
	}

	@Override
	public Page<Area> pageQuery(Pageable pageable) {
		return areaDao.findAll(pageable);
	}

	@Override
	public List<Area> findAll() {
		return areaDao.findAll();
	}

	@Override
	public List<Area> findByQ(String q) {
		return areaDao.findByQ(q);
	}

}
