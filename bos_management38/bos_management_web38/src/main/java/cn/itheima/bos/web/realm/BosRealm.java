package cn.itheima.bos.web.realm;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import cn.itheima.bos.dao.system.IPermissionDao;
import cn.itheima.bos.dao.system.IRoleDao;
import cn.itheima.bos.dao.system.IUserDao;
import cn.itheima.bos.domain.system.Permission;
import cn.itheima.bos.domain.system.Role;
import cn.itheima.bos.domain.system.User;
/**
 * 自定义realm 认证  授权
 * @author admin
 *
 */
public class BosRealm extends AuthorizingRealm {

	@Resource
	private IUserDao userDao;
	
	
	@Resource
	private IRoleDao roleDao;//角色dao
	
	
	@Resource
	private IPermissionDao permissionDao;//单个权限dao
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*//授权对象  为当前用户授权area
		SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
		sai.addStringPermission("area");
		return sai;*/
		
		SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
		
		///获取当前用户信息
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		String username = user.getUsername();//用户名
		if(StringUtils.isNotBlank(username) && username.equals("admin")){
			//为管理员授权
			//查询角色表
			List<Role> listRole = roleDao.findAll();
			if(listRole != null && listRole.size() > 0){
				for (Role role : listRole) {
					sai.addRole(role.getKeyword());//为当前管理员用户授予角色表中关键字
				}
			}
			
			//查询权限表
			List<Permission> listPermission = permissionDao.findAll();
			if(listPermission != null && listPermission.size() > 0){
				for (Permission permission : listPermission) {
					sai.addStringPermission(permission.getKeyword());//为当前管理员用户授予角色表中关键字
				}
			}
		}
		else
		{
			//为普通用户授权
			int id = user.getId();///用户id
			
			List<Role> listRole =roleDao.findRoleByUserId(id);
			if(listRole != null && listRole.size() > 0){
				for (Role role : listRole) {
					sai.addRole(role.getKeyword());//为当前管理员用户授予角色表中关键字
				}
			}
			
			//查询权限表
			List<Permission> listPermission = permissionDao.findPermissionByUserId(id);
			if(listPermission != null && listPermission.size() > 0){
				for (Permission permission : listPermission) {
					sai.addStringPermission(permission.getKeyword());//为当前管理员用户授予角色表中关键字
				}
			}
			
		}
		return sai;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//AuthenticationToken  前台传入的token用户名 密码
		UsernamePasswordToken upt = (UsernamePasswordToken)token;
		String username = upt.getUsername();///用户名
		//根据token中的用户名查询用户是否存在 （需要程序员编码查询数据库）
		if(StringUtils.isNotBlank(username)){
			User u= userDao.findByUsername(username);
			if(u != null){
				//用户对象   凭证：数据库中的密码 realmName:唯一标识cn.itheima.bos.web.realm.BosRealm
				//将查询出来的数据库中的密码  交给认证对象
				return new SimpleAuthenticationInfo(u, u.getPassword(), this.getName());
			}
		}
		//认证对象为空 表示账号不存在 抛出账号不存在异常
		return null;
	}

}
