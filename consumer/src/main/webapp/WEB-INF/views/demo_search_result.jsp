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
	<!-- HashMap<文档id, Map<字段名, 字段值>> -->
	<table>
		<c:if test="${empty searchResult }">
			<tr><td>没有匹配的查询结果！</td></tr>
		</c:if>
		<c:if test="${!empty searchResult }">
			<tr>
				<td>昵称</td>
				<td>性别</td>
				<td>职业</td>
				<td>家乡</td>
				<td>照片</td>
				<td>详情</td>
			</tr>
			<c:forEach items="${searchResult }" var="entry">
				
				<tr>
					<td>${entry.value['user_nick'] }</td>
					<td>${entry.value['user_gender'] }</td>
					<td>${entry.value['user_job'] }</td>
					<td>${entry.value['user_hometown'] }</td>
					<td>
						<c:if test="${empty entry.value['user_pic_group'] }">暂时没有上传照片</c:if>
						<c:if test="${!empty entry.value['user_pic_group'] }">
							<img src="http://192.168.0.247/${entry.value['user_pic_group'] }/${entry.value['user_pic_file_name'] }"/>
						</c:if>
					</td>
					<td>
						<a href="demo/center/${entry.value['id'] }">详情</a>
					</td>
				</tr>
				
			</c:forEach>
		</c:if>
	</table>
	
	<br/><br/>
	
	<a href="index.jsp">回首页</a>

</body>
</html>