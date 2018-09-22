package cn.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itheima.bos.domain.base.Standard;

public interface IStandardDao extends JpaRepository<Standard, Integer> {
	// 2、根据标准的方法命名规则，框架可以自动提供实现 (查询)
	/**
	 * 根据收派标准名称查询收派标准信息 规则 ：findBy+属性名称 首字母大写
	 * 
	 * @param string
	 * @return
	 */
	Standard findByName(String name);

	/**
	 * 模糊查询
	 * 
	 * @param name
	 * @return
	 */
	Standard findByNameLike(String name);

	/**
	 * 跟取派名称和操作查询收派标准
	 * 
	 * @param name
	 * @param operator
	 * @return
	 */
	Standard findByNameAndOperator(String name, String operator);

	// 3、 如果方法命名不符合命名规范，可以使用Query注解指定JPQL(HQL)或者SQL 接口
	// 如果执行的sql语句一定要,nativeQuery=true
	// sql
	@Query(value = "select * from t_standard where c_name = ?", nativeQuery = true)
	Standard findByNamex(String string);

	@Query(value = "select * from t_standard where c_name = ? and C_OPERATOR = ?", nativeQuery = true)
	Standard findByNameAndOperatorxx(String string, String string2);

	@Query(value = "select * from t_standard where c_name like ?", nativeQuery = true)
	Standard findByNameLikexxx(String string);

	// jpql
	@Query(value = "from Standard where name = ?")
	Standard findByNamexx(String name);

	@Query(value = "from Standard where name = ?2 and operator = ?1")
	Standard findByNameAndOperatorxxx(String operator, String name);

	@Query(value = "from Standard where name like ?")
	Standard findByNameLikexxxx(String string);

}
