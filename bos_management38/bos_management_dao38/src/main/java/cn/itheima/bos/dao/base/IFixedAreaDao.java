package cn.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itheima.bos.domain.base.FixedArea;

public interface IFixedAreaDao extends JpaRepository<FixedArea, String>{

	FixedArea findById(String id);
}
