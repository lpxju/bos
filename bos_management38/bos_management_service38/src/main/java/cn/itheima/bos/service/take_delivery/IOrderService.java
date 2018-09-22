package cn.itheima.bos.service.take_delivery;

import cn.itheima.bos.domain.take_delivery.Order;

/**
 * 在线下单功能接口
 * @author admin
 *
 */
public interface IOrderService {

	public void save(Order order);
}
