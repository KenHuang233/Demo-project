<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Demo练习</title>
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

	<form action="demo/login" method="post">
		<table>
			<c:if test="${!empty requestScope.message }">
				<tr><td colspan="2">${requestScope.message }</td></tr>
			</c:if>
			<tr>
				<td>账号</td>
				<td>
					<input type="text" name="userName"/>
				</td>
			</tr>
			<tr>
				<td>密码</td>
				<td>
					<input type="text" name="userPwd"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="登录"/>
				</td>
			</tr>
		</table>
	</form>
	
	<br/><br/>
	<a href="index.jsp">回首页</a>

</body>
</html>