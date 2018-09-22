package cn.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itheima.bos.domain.base.TakeTime;

public interface ITakeTimeDao extends JpaRepository<TakeTime, Integer> {

}
