<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 引入shiro标签库 -->
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="../js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../js/easyui/themes/icon.css">
<script type="text/javascript" src="../js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
</head>
<body>
<!-- 定一个容器 用于存放数据表格 -->
	<shiro:hasPermission name="area">
	<input type="button" value="xxx">
	</shiro:hasPermission>
	<br/>
	<!-- 测试所有的shiro标签库 -->
	<shiro:authenticated>shiro:authenticated</shiro:authenticated><br/>
	<shiro:guest>shiro:guest</shiro:guest><br/>
	<shiro:hasAnyRoles name="xxx">shiro:hasAnyRoles</shiro:hasAnyRoles><br/>
	<shiro:hasPermission name="area">shiro:hasPermission</shiro:hasPermission><br/>
	<shiro:hasRole name="xxx">shiro:hasRole</shiro:hasRole><br/>
	<shiro:lacksPermission name="areaxxx">shiro:lacksPermission</shiro:lacksPermission><br/>
	<shiro:lacksRole name="xxx">shiro:lacksRole</shiro:lacksRole><br/>
	<shiro:notAuthenticated>shiro:notAuthenticated</shiro:notAuthenticated><br/>
	<shiro:principal property="password"></shiro:principal><br/>
	<shiro:user>shiro:user</shiro:user><br/>
	
	<table id="dg" style="width: 605px"></table>
	<script type="text/javascript">
		$(function(){
			$('#dg').datagrid(
								{ ///json数据格式
				
									url:'../data/datagrid_data.json', //请求的json数据
									columns:[[ //columns 标题行 （包含多列）
										{field:'id',title:'员工编号',width:100}, 
										{field:'name',title:'员工姓名',width:100}, 
										{field:'sal',title:'员工薪资',width:100,align:'right'} 
									]],
									//工具栏
									toolbar: [
											      {  		
												  iconCls: 'icon-add',  //按钮样式
												  text:'增加',//按钮标题
												  handler: function() //回调函数
												  	  {
													    alert('edit')
													  }  	
												  },
												  {  		
												  iconCls: 'icon-edit',  //按钮样式
												  text:'修改',//按钮标题
												  handler: function() //回调函数
												  	  {
													    alert('edit')
													  }  	
												  },
												  <shiro:hasPermission name="delete">
												  
												  {  		
												  iconCls: 'icon-cancel',  //按钮样式
												  text:'作废',//按钮标题
												  handler: function() //回调函数
												  	  {
													    alert('edit')
													  }  	
												  },
												  </shiro:hasPermission>
												  
												  {  		
												  iconCls: 'icon-save',  //按钮样式
												  text:'还原',//按钮标题
												  handler: function() //回调函数
												  	  {
													    alert('edit')
													  }  	
												  }
										  	  ],
										pagination:true, // 启用分页
										pageSize:3,//当设置分页属性,初始化页面大小 pageSize必须要在pageList值列表中
										pageList:[1,2,3,4,5]//pageList下拉列表值设置
								}
							); 
		})
	</script>
xxxxx
</body>
</html>