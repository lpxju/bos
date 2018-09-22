package cn.itheima.bos.service.take_delivery;
import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itheima.bos.domain.take_delivery.WayBill;
/**
 * 运单模块接口
 * @author admin
 *
 */
public interface IWaybillService {

	/**
	 * 保存运单数据
	 * @param model
	 */
	public void save(WayBill model);
	

	public List<String> importXls(File waybillFile);

	public Page<WayBill> pageQuery(Pageable pageable);

}