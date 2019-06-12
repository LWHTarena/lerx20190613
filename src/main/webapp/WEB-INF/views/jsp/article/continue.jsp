<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page" ></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>操作成功！</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
<script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/js/layui/css/global.css">
<style>

.site-title {text-align:center;font-size: 20px;font-weight: bold;margin-top: 40px;color: green; }
.layui-btn {margin-top: 30px;}
</style>
</head>

<body>
<div class="container">
	<div class="site-title">
		<i class="layui-icon layui-icon-ok" style="font-size: 30px; "></i> 操作成功！
		
	</div>
	<div class="site-title">
		<a href="${pageContext.request.contextPath}/action_article/beforeAdd?gid=${requestScope.parm }"><i class="layui-icon layui-icon-right">继续增加</i></a> 
		
	</div>

</div>

</body>
</html>