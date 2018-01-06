<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
  ~ Copyright (c) 2018, Xuliang Huang. All rights reserved.
  --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Demo练习</title>
<base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/"/>
</head>
<body>

	<a href="demo/test">Test Demo</a>
	
	<c:if test="${empty sessionScope.loginUser }">
		<a href="demo/toLoginPage">登录</a>
		|
		<a href="demo/toRegistPage">注册</a>
	</c:if>
	<c:if test="${!empty sessionScope.loginUser }">
		欢迎您！${sessionScope.loginUser.userName }
		<a href="demo/logout">退出登录</a>
		<a href="demo/center/${sessionScope.loginUser.userId }">个人中心</a>
	</c:if>

	<br/><br/>

	<form action="demo/search">
		<input type="text" name="keywords"/>
		<input type="submit" value="搜索"/>
	</form>
	
	<!-- <hr/> -->
	<!-- 
		一、表单要求
			1.请求方式必须是POST
			2.请求体的编码方式应该是multipart/form-data
			3.使用input type="file"生成文件上传框
		二、SpringMVC环境要求
			1.FileUpload组件
				需要引入commons-fileupload的依赖
			2.在SpringMVC配置文件中配置CommonsMultipartResolver的bean
				bean的id必须是multipartResolver
				必须开启mvc:annotation-driven
		三、handler方法
			1.使用@RequestParam注解接收文件上传框提交的数据
			2.使用MultiPartFile接口类型接收具体文件上传数据
	 -->
	<%--<form action="demo/testUpload" method="post" enctype="multipart/form-data">--%>
		<%--<input type="file" name="uploadFile"/>--%>
		<%--<input type="submit" value="上传"/>--%>
	<%--</form>--%>

</body>
</html>