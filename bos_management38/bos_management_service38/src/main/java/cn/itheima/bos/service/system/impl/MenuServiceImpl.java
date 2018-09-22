package cn.itheima.bos.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itheima.bos.dao.system.IMenuDao;
import cn.itheima.bos.domain.system.Menu;
import cn.itheima.bos.domain.system.User;
import cn.itheima.bos.service.system.IMenuService;
/**
 * 菜单模块（业务逻辑处理层接口实现类）
 * @author admin
 *
 */
@Service
@Transactional
public class MenuServiceImpl implements IMenuService {

	@Resource
	private IMenuDao menuDao;
	
	@Override
	public List<Menu> findByParentMenuIsNull() {
		//select * from t_menu t where  c_pid is null
		return menuDao.findByParentMenuIsNull();
	}

	@Override
	public void save(Menu model) {
		if(model.getParentMenu()!=null && model.getParentMenu().getId() == 0){
			model.setParentMenu(null);//parentMenu没有选中父菜单的时候，默认值为0，表中并没有主键id,重置设置parentMenu为null.
		}
		menuDao.save(model);
	}

	@Override
	public Page<Menu> pageQuery(Pageable pageable) {
		return menuDao.findAll(pageable);
	}

	@Override
	public List<Menu> findMenuByUserId() {
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		String username = user.getUsername();//用户名
		List<Menu> listMenu = null;
		if(StringUtils.isNotBlank(username) && username.equals("admin")){
			listMenu = menuDao.findAll();
		}else{
			listMenu = menuDao.findMenuByUserId(user.getId());
		}
		return listMenu;
	}

}
