package cn.itheima.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itheima.bos.domain.take_delivery.WorkBill;

public interface IWorkBillDao extends JpaRepository<WorkBill, Integer> {

}
