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
<title>用户详细信息</title>
<base href="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/"/>
<style type="text/css">
	
	table {
		border: 1px solid black;
		border-collapse: collapse;
	}
	
	table td{
		border: 1px solid black;
	}
	
</style>
</head>
<body>

	<table>
		<c:if test="${requestScope.detail == null }">
			<tr>
				<td>尚未创建用户信息！<a href="demo/toModifyPage/${requestScope.userId }">点这里完善信息！</a></td>
			</tr>
		</c:if>
		<c:if test="${requestScope.detail != null }">
			<tr>
				<td>昵称</td>
				<td>${requestScope.detail.userNick }</td>
			</tr>
			<tr>
				<td>性别</td>
				<td>${(requestScope.detail.userGender==0)?'男':'女' }</td>
			</tr>
			<tr>
				<td>职业</td>
				<td>${requestScope.detail.userJob }</td>
			</tr>
			<tr>
				<td>家乡</td>
				<td>${requestScope.detail.userHometown }</td>
			</tr>
			<tr>
				<td>自我介绍</td>
				<td>${requestScope.detail.userDesc }</td>
			</tr>
			<tr>
				<td>照片</td>
				<td>
					<c:if test="${empty requestScope.detail.userPicGroup }">
						暂无照片
					</c:if>
					<c:if test="${!empty requestScope.detail.userPicGroup }">
						<img src="http://192.168.0.247/${requestScope.detail.userPicGroup }/${requestScope.detail.userPicFileName}"/>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<a href="demo/toModifyPage/${requestScope.userId }">点这里更新信息！</a>
				</td>
			</tr>
		</c:if>
	</table>
	
	<br/><br/>
	
	<a href="index.jsp">回首页</a>
	
	<br/><br/>

</body>
</html>