package cn.itheima.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itheima.bos.domain.take_delivery.WayBill;
/**
 * 运单模块dao持久层
 * @author admin
 *
 */
public interface IWaybillDao extends JpaRepository<WayBill, Integer> {

	WayBill findByWayBillNum(String wayBillNum);

}
